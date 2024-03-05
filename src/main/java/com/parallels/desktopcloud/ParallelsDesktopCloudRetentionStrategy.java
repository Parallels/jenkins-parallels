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

import hudson.slaves.RetentionStrategy;
import java.util.logging.Level;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;


public class ParallelsDesktopCloudRetentionStrategy extends RetentionStrategy<ParallelsDesktopVMSlaveComputer>
{
	private static final ParallelsLogger LOGGER = ParallelsLogger.getLogger("PDCloudRetentionStrategy");

	@DataBoundConstructor
	public ParallelsDesktopCloudRetentionStrategy()
	{
		super();
	}

	@Override
	public long check(ParallelsDesktopVMSlaveComputer c)
	{
		LOGGER.log(Level.SEVERE, "Check VM computer %s", c.getName());
		if (c.isOffline())
			return 1;
		if (c.isIdle())
		{
			try
			{
				LOGGER.log(Level.SEVERE, "Disconnecting computer...");
				c.disconnect(null).get();
				c.getNode().terminate();
			}
			catch (Exception e)
			{
				LOGGER.log(Level.SEVERE, "Error: %s", e);
			}
		}
		return 1;
	}

	@Override
	public void start(ParallelsDesktopVMSlaveComputer c)
	{
		LOGGER.log(Level.SEVERE, "Starting VM computer %s", c.getName());
		c.connect(false);
	}

	@Override
	public DescriptorImpl getDescriptor()
	{
		return DESCRIPTOR;
	}

	@Restricted(NoExternalUse.class)
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
	public static final class DescriptorImpl extends hudson.model.Descriptor<RetentionStrategy<?>>
	{
		@Override
		public String getDisplayName()
		{
			return "ParallelsDesktop Cloud Retention Strategy";
		}
	}
}
