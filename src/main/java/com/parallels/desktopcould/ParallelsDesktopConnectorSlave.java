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

import hudson.model.Computer;
import hudson.model.Descriptor;
import hudson.model.Slave;
import hudson.model.Node.Mode;
import hudson.slaves.ComputerLauncher;
import hudson.slaves.NodeProperty;
import hudson.slaves.RetentionStrategy;
import hudson.Extension;
import java.io.IOException;
import java.util.ArrayList;
import org.kohsuke.stapler.DataBoundConstructor;


public class ParallelsDesktopConnectorSlave extends Slave
{
	@DataBoundConstructor
	public ParallelsDesktopConnectorSlave(String name, String remoteFS, ComputerLauncher launcher)
			throws IOException, Descriptor.FormException
	{
		super(name, "", remoteFS, 1, Mode.NORMAL, "", launcher,
				new RetentionStrategy.Demand(1, 1), new ArrayList<NodeProperty<?>>());
	}

	@Override
	public Computer createComputer()
	{
		return new ParallelsDesktopConnectorSlaveComputer(this);
	}

	@Override
	public String getRemoteFS()
	{
		String res = super.getRemoteFS();
		return res;
	}

	@Extension
	public static final class DescriptorImpl extends SlaveDescriptor
	{
		@Override
		public String getDisplayName()
		{
			return "Parallels Desktop connector slave";
		}

		@Override
		public boolean isInstantiable()
		{
			return false;
		}
	}
}
