package koncept.disjunction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import koncept.disjunction.describe.MethodInvocation;
import koncept.disjunction.describe.MethodResult;
import koncept.disjunction.loopback.DirectLoopbackRegistry;
import koncept.disjunction.loopback.LoopbackTransport;
import koncept.disjunction.service.DisjunctionClient;
import koncept.disjunction.service.DisjunctionServer;
import koncept.disjunction.test.CallTracker;
import koncept.disjunction.test.CallTrackerService;

import org.junit.Test;

/**
 * Initial bootstrapping tests
 * @author koncept
 *
 */
public class LoopbackMessagingTest {

	@Test
	public void methodCallViaReflection() throws Exception {
		DisjunctionServer dj = new DisjunctionServer();
		String name = "CallTracker";
		CallTrackerService tracker = new CallTrackerService();
		dj.expose(name, tracker);
		
		//direct loopback - basically the same as a method call
		DisjunctionRegistry localRegistry = new DirectLoopbackRegistry(dj.getObjects());
		
		CallTracker remoteTracker = (CallTracker)localRegistry.lookup(name);
		
		assertNotEquals(tracker, remoteTracker);
		
		int originalCount = tracker.methodInvocationCount.get();
		for(int i = 0; i < 10; i++) {
			assertThat(remoteTracker.methodInvocationCount(), is(originalCount + i));
		}
	}
	
	@Test
	public void methodCallViaLoopbackTransport() throws Exception {
		DisjunctionServer dj = new DisjunctionServer();
		String name = "CallTracker";
		CallTrackerService tracker = new CallTrackerService();
		dj.expose(name, tracker);
		
		LoopbackTransport<MethodInvocation, MethodResult> loopback = new LoopbackTransport<MethodInvocation, MethodResult>();
		dj.addTransport(loopback);
		
		DisjunctionClient djc = new DisjunctionClient(loopback.looped());
		
		
		
		List<String> names = djc.list();
		assertTrue(names.contains(name));
		
		
		//messaging loopback - to POJO back, no serialization
		CallTracker remoteTracker = (CallTracker)djc.lookup(name);
		
		assertNotEquals(tracker, remoteTracker);
		
		int originalCount = tracker.methodInvocationCount.get();
		for(int i = 0; i < 10; i++) {
			assertThat(remoteTracker.methodInvocationCount(), is(originalCount + i));
		}
	}
	
}
