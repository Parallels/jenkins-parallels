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

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.slaves.ComputerLauncher;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;


public class ParallelsDesktopVM implements Describable<ParallelsDesktopVM>
{
	private String vmid;
	private String labels;
	private String remoteFS;
	private String slaveName;
	private ComputerLauncher launcher;

	@DataBoundConstructor
	public ParallelsDesktopVM(String vmid, String labels, String remoteFS, ComputerLauncher launcher)
	{
		this.vmid = vmid;
		this.labels = labels;
		this.remoteFS = remoteFS;
		this.launcher = launcher;
	}

	public String getVmid()
	{
		return vmid;
	}
	
	public String getLabels()
	{
		return labels;
	}

	public String getRemoteFS()
	{
		return remoteFS;
	}

	public ComputerLauncher getLauncher()
	{
		return launcher;
	}

	public void setSlaveName(String slaveName)
	{
		this.slaveName = slaveName;
	}

	public String getSlaveName()
	{
		return slaveName;
	}

	@Override
	public Descriptor<ParallelsDesktopVM> getDescriptor()
	{
		return Jenkins.getInstance().getDescriptor(getClass());
	}

	@Extension
	public static final class DescriptorImpl extends Descriptor<ParallelsDesktopVM>
	{
		@Override
		public String getDisplayName()
		{
			return "Parallels Desktop virtual machine";
		}
	}
}
