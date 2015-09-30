/*
 * The MIT License
 *
 * (c) 2004-2015. Parallels IP Holdings GmbH. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.parallels.desktopcloud;

import java.lang.reflect.Field;

import hudson.model.Node;
import hudson.remoting.Channel;
import hudson.security.Permission;
import hudson.slaves.AbstractCloudComputer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.security.MasterToSlaveCallable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class ParallelsDesktopConnectorSlaveComputer extends AbstractCloudComputer<ParallelsDesktopConnectorSlave>
{
	private static final Logger LOGGER = Logger.getLogger("PDConnectorSlaveComputer");

	public ParallelsDesktopConnectorSlaveComputer(ParallelsDesktopConnectorSlave slave)
	{
		super(slave);
	}

	public boolean checkVmExists(String vmId)
	{
		try
		{
			RunVmCallable command = new RunVmCallable("list", "-i", "--json");
			Channel channel = getChannel();
			if (channel == null)
			{
				LOGGER.log(Level.SEVERE, "Connecting to node");
				connect(false).get();
			}
			channel = getChannel();
			String callResult = channel.call(command);
			JSONArray vms = (JSONArray)JSONSerializer.toJSON(callResult);
			for (int i = 0; i < vms.size(); i++)
			{
				JSONObject vmInfo = vms.getJSONObject(i);
				if (vmId.equals(vmInfo.getString("ID")) || vmId.equals(vmInfo.getString("Name")))
					return true;
			}
			return true;
		}
		catch (Exception ex)
		{
			LOGGER.log(Level.SEVERE, ex.toString());
		}
		return false;
	}

	private String getVmIPAddress(String vmId) throws Exception
	{
		int TIMEOUT = 60;
		for (int i = 0; i < TIMEOUT; ++i)
		{
			RunVmCallable command = new RunVmCallable("list", "-f", "--json", vmId);
			String callResult = getChannel().call(command);
			LOGGER.log(Level.SEVERE, " - (" + i + "/" + TIMEOUT + ") calling for IP");
			LOGGER.log(Level.SEVERE, callResult);
			JSONArray vms = (JSONArray)JSONSerializer.toJSON(callResult);
			JSONObject vmInfo = vms.getJSONObject(0);
			String ip = vmInfo.getString("ip_configured");
			if (!ip.equals("-"))
				return ip;
			Thread.sleep(1000);
		}
		throw new Exception("Failed to get IP for VM '" + vmId + "'");
	}

	public Node createSlaveOnVM(ParallelsDesktopVM vm) throws Exception
	{
		String vmId = vm.getVmid();
		String slaveName = vm.getSlaveName();
		LOGGER.log(Level.SEVERE, "Starting slave '" + slaveName+ "'");
		LOGGER.log(Level.SEVERE, "Starting virtual machine '" + vmId + "'");
		RunVmCallable command = new RunVmCallable("start", vmId);
		getChannel().call(command);
		LOGGER.log(Level.SEVERE, "Waiting for IP...");
		String ip = getVmIPAddress(vmId);
		LOGGER.log(Level.SEVERE, "Got IP address for VM " + vmId + ": " + ip);
		try
		{
			Class<?> c = vm.getLauncher().getClass();
			Field f = c.getDeclaredField("host");
			f.setAccessible(true);
			f.set(vm.getLauncher(), ip);
			f.setAccessible(false);
		}
		catch (NoSuchFieldException x)
		{
			LOGGER.log(Level.SEVERE, "No 'host' field in launcher of " + slaveName);
		}
		return new ParallelsDesktopVMSlave(vm, this);
	}

	public void stopVM(String vmId)
	{
		try
		{
			LOGGER.log(Level.SEVERE, "Suspending...");
			RunVmCallable command = new RunVmCallable("suspend", vmId);
			String res = getChannel().call(command);
			LOGGER.log(Level.SEVERE, res);
		}
		catch (Exception ex)
		{
			LOGGER.log(Level.SEVERE, ex.toString());
		}
	}

	private static final class RunVmCallable extends MasterToSlaveCallable<String, IOException>
	{
		private static final String cmd = "/usr/local/bin/prlctl";
		private final String[] params;
		
		public RunVmCallable(String... params)
		{
			this.params = params;
		}

		@Override
		public String call() throws IOException
		{
			List<String> cmds = new ArrayList<String>();
			cmds.add(cmd);
			cmds.addAll(Arrays.asList(this.params));
			
			LOGGER.log(Level.SEVERE, "Running command:");
			for (String s: cmds)
				LOGGER.log(Level.SEVERE, " [" + s + "]");
			ProcessBuilder pb = new ProcessBuilder(cmds);
			pb.redirectErrorStream(true);
			Process pr = pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			String result = "";
			while ((line = in.readLine()) != null) 
			{
				result += line;
			}
			try
			{
				pr.waitFor();
			}
			catch (InterruptedException ex)
			{
				LOGGER.log(Level.SEVERE, ex.toString());
			}
			return result;
		}
	}
	
	@Override
	public boolean hasPermission(Permission permission)
	{
		if (permission == CONFIGURE)
			return false;
		return super.hasPermission(permission);
	}
}
