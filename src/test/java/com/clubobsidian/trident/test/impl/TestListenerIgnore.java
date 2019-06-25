/*  
   Copyright 2019 Club Obsidian and contributors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.clubobsidian.trident.test.impl;

import com.clubobsidian.trident.EventHandler;
import com.clubobsidian.trident.EventPriority;

public class TestListenerIgnore {

	private boolean canceled = false;
	private boolean ignored = true;

	public boolean isCanceled()
	{
		return this.canceled;
	}
	
	public boolean getIgnored()
	{
		return this.ignored;
	}
	
	@EventHandler
	public void setCanceled(TestCancelableEvent e)
	{
		e.setCanceled(true);
		this.canceled = true;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCanceled = true)
	public void test(TestCancelableEvent e)
	{
		this.ignored = false;
	}
}