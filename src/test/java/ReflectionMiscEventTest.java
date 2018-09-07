/*   Copyright 2018 Club Obsidian and contributors.

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
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

import com.clubobsidian.trident.Listener;
import com.clubobsidian.trident.MethodExecutor;
import com.clubobsidian.trident.impl.reflection.ReflectionMethodExecutor;

public class ReflectionMiscEventTest {

	@Test
	public void methodExecutorTest()
	{
		try 
		{
			Listener listener = new TestListener("test");
			Method testEventMethod = listener.getClass().getDeclaredMethod("test", TestEvent.class);
			MethodExecutor executor = new ReflectionMethodExecutor(listener, testEventMethod);
			
			assertTrue("Listeners are not equal for method executor", listener.equals(executor.getListener()));
			assertTrue("Methods are not equal for method executor", testEventMethod.equals(executor.getMethod()));
		} 
		catch (NoSuchMethodException | SecurityException e) 
		{
			e.printStackTrace();
		}		
	}
}
