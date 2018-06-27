import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.clubobsidian.trident.EventManager;
import com.clubobsidian.trident.impl.javaassist.JavaAssistEventManager;

public class JavaAssistEventManagerTest {

	@Test
	public void testEventFiring()
	{
		TestListener test = new TestListener("test");
		EventManager manager = new JavaAssistEventManager();
		boolean registered = manager.register(test);
		
		assertTrue("Event is not registered", registered);
		assertTrue("Test is not false", !test.getTest());
		
		manager.dispatchEvent(new TestEvent());
		
		assertTrue("Test is not true", test.getTest());
	}
}