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

import hudson.model.Descriptor;
import hudson.slaves.AbstractCloudSlave;
import hudson.model.TaskListener;
import hudson.slaves.AbstractCloudComputer;
import hudson.model.Node.Mode;
import hudson.slaves.NodeProperty;
import hudson.Extension;
import hudson.model.Node;
import hudson.slaves.EphemeralNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.kohsuke.stapler.DataBoundConstructor;


public class ParallelsDesktopVMSlave extends AbstractCloudSlave implements EphemeralNode
{
	private static final ParallelsLogger LOGGER = ParallelsLogger.getLogger("PDVMSlave");
	private final transient ParallelsDesktopConnectorSlaveComputer connector;
	private final ParallelsDesktopVM vm;

	@DataBoundConstructor
	public ParallelsDesktopVMSlave(ParallelsDesktopVM vm, ParallelsDesktopConnectorSlaveComputer connector)
			throws IOException, Descriptor.FormException
	{
		super(vm.getSlaveName(), "", vm.getRemoteFS(), 1, Mode.NORMAL, vm.getLabels(), vm.getLauncher(),
				new ParallelsDesktopCloudRetentionStrategy(), new ArrayList<NodeProperty<?>>());
		this.connector = connector;
		this.vm = vm;
	}

	@Override
	public AbstractCloudComputer createComputer()
	{
		return new ParallelsDesktopVMSlaveComputer(this);
	}

	@Override
	protected void _terminate(TaskListener tl) throws IOException, InterruptedException
	{
		LOGGER.log(Level.SEVERE, "!!! Terminating slave node '%s', id '%s'", getNodeName(), vm.getVmid());
		connector.postBuildAction(vm);
		vm.onSlaveReleased(this);
		LOGGER.log(Level.SEVERE, "Node was terminated.");
	}

	@Override
	public Node asNode()
	{
		return this;
	}

	@Extension
	public static final class DescriptorImpl extends SlaveDescriptor
	{
		@Override
		public String getDisplayName()
		{
			return "Parallels Desktop VM slave";
		}

		@Override
		public boolean isInstantiable()
		{
			return false;
		}
	}
}
