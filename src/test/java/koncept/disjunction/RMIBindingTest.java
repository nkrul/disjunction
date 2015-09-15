package koncept.disjunction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import koncept.disjunction.test.CallTracker;
import koncept.disjunction.test.CallTrackerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A test class for demonstrating different way to bind to an RMI Registry - and some of the limitations there are.<br>
 * 
 * TODO: http://docs.oracle.com/javase/8/docs/technotes/guides/rmi/socketfactory/
 * 
 * 
 * @author koncept
 *
 */
public class RMIBindingTest {
	//if a zero is passed in, it will default to 1099 anyway
	private static final int testPort = 1098; //1099 = default port

	@Before
	public void setup() throws Exception {
		//this can be used to override an rmi registry default lookup hostname
//		System.setProperty("java.rmi.server.hostname", "localhost");
		
		try {
			LocateRegistry.createRegistry(1099);
		} catch (ExportException e) {
			//rebind. because Java RMI has *NO WAY* of fully unbinding when using the 'default' way of doing things
			if (!e.getMessage().equals("internal error: ObjID already in use"))
				throw e;
		}
	}
	
	@After
	public void teardown() throws Exception {
		Registry registry = LocateRegistry.getRegistry();
		
		for(String name: registry.list()) try {
//			System.out.println("unbinding: " + name); //unbinding: CallTracker
			registry.unbind(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void javaRmiViaLocator() throws Exception {
		CallTrackerService tracker = new CallTrackerService();
		Remote stub =  UnicastRemoteObject.exportObject(tracker, testPort);
		String name = "CallTracker";
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind(name, stub);

		CallTracker remoteTracker = (CallTracker)registry.lookup(name);
		
		int originalCount = tracker.methodInvocationCount.get();
		for(int i = 0; i < 10; i++) {
			assertThat(remoteTracker.methodInvocationCount(), is(originalCount + i));
		}
	}
	
	@Test
	public void javaRmiViaNaming() throws Exception {
		CallTrackerService tracker = new CallTrackerService();
		String name = "CallTracker";
		Remote stub =  UnicastRemoteObject.exportObject(tracker, testPort);
		Naming.bind(name, stub);

		CallTracker remoteTracker = (CallTracker)Naming.lookup(name);
		
		int originalCount = tracker.methodInvocationCount.get();
		for(int i = 0; i < 10; i++) {
			assertThat(remoteTracker.methodInvocationCount(), is(originalCount + i));
		}
	}
	
	@Test(expected=NotBoundException.class)
	public void nonExistentGet() throws Exception {
		CallTrackerService tracker = new CallTrackerService();
		String name = "CallTracker";
		Remote stub =  UnicastRemoteObject.exportObject(tracker, testPort);
		Naming.bind(name, stub);

		CallTracker remoteTracker = (CallTracker)Naming.lookup("invalid");
		fail("unreachable");
	}
	
}
