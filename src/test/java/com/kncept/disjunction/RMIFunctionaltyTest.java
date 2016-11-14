package com.kncept.disjunction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kncept.disjunction.test.CheckedTestException;
import com.kncept.disjunction.test.LoopbackRmiSource;
import com.kncept.disjunction.test.NonRemoteCallTracker;
import com.kncept.disjunction.test.NonRemoteCallTrackerService;
import com.kncept.disjunction.test.RmiSource;
import com.kncept.disjunction.test.NonRemoteCallTracker.ThreadIdCallback;

public class RMIFunctionaltyTest {
	private final RmiSource rmiSource = new LoopbackRmiSource();

	private NonRemoteCallTrackerService localTracker;
	private NonRemoteCallTracker remoteTracker;
	
	@Before
	public void init() {
		localTracker = new NonRemoteCallTrackerService();
		rmiSource.start();
		rmiSource.serverExpose(NonRemoteCallTracker.class.getSimpleName(), localTracker);
		remoteTracker =  (NonRemoteCallTracker)rmiSource.clientLookup(NonRemoteCallTracker.class.getSimpleName());
	}
	
	@After
	public void destroy() {
		rmiSource.stop();
	}
	
	@Test
	public void canRemoteAnyInterface() {
		assertNotNull(remoteTracker);
	}
	
	@Test
	public void clientListingContainsExpectedName() {
		assertTrue(rmiSource.clientNames().contains(NonRemoteCallTracker.class.getSimpleName()));
	}
	
	@Test
	public void simpleMethodInvocation() throws Exception {
		int originalCount = localTracker.methodInvocationCount.get();
		for(int i = 0; i < 10; i++) {
			assertThat(remoteTracker.methodInvocationCount(), is(originalCount + i));
		}
		assertThat(localTracker.methodInvocationCount.get(), is(originalCount + 10));
	}
	
	@Test
	public void checkedTestException() throws Exception {
		int originalCount = localTracker.checkedTestException.get();
		for(int i = 0; i < 10; i++) {
			try {
				remoteTracker.checkedTestException();
				fail("unreachable");
			} catch (CheckedTestException e) {
				//expected
			}
		}
		assertThat(localTracker.checkedTestException.get(), is(originalCount + 10));
	}
	
	@Test
	public void callbackTest() throws Exception {
		//check that the thread id is different ... calls are being 'remoted' in some way
		String localThreadId = new ThreadIdCallback().call();
		String remoteThreadId = remoteTracker.callback(new ThreadIdCallback());
		assertNotEquals(remoteThreadId, localThreadId);
	}
	
	@Test
	public void interfacesTest() throws Exception {
		//mmm... this is simpler than expeced... Java RMI is nice and clean here :)
		assertThat(localTracker.getClass().getInterfaces().length, is(1));
		assertThat(remoteTracker.getClass().getInterfaces().length, is(1));
	}
	
}
