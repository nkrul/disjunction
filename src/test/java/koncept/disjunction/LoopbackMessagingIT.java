package koncept.disjunction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import koncept.disjunction.test.CallTracker;
import koncept.disjunction.test.CallTrackerService;

import org.junit.Test;

public class LoopbackMessagingIT {

	@Test
	public void basicLoopback() throws Exception {
		DisjunctionServer dj = new DisjunctionServer();
		String name = "CallTracker";
		CallTrackerService tracker = new CallTrackerService();
		dj.expose(name, tracker);
		
		CallTracker remoteTracker = (CallTracker)dj.lookup(name);
		
		assertNotEquals(tracker, remoteTracker);
		
		int originalCount = tracker.methodInvocationCount.get();
		for(int i = 0; i < 10; i++) {
			assertThat(remoteTracker.methodInvocationCount(), is(originalCount + i));
		}
	}
	
}
