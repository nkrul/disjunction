package koncept.disjunction;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import koncept.disjunction.test.CallTracker;
import koncept.disjunction.test.CallTrackerService;
import koncept.disjunction.test.CheckedTestException;
import koncept.disjunction.test.JavaRmiSource;
import koncept.disjunction.test.LoopbackRmiSource;
import koncept.disjunction.test.RmiSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RMIEquivalenceTest {
	
	private final RmiSource rmiSource;
	
	private CallTrackerService localTracker;
	private CallTracker remoteTracker;
	
	@Parameters
	public static List<Object[]> params() throws Exception {
		return asList(
				new Object[]{new LoopbackRmiSource()},
				new Object[]{new JavaRmiSource()}
		);
	}
	
	public RMIEquivalenceTest(RmiSource rmiSource) {
		this.rmiSource = rmiSource;
	}
	
	@Before
	public void initRmiSource() {
		rmiSource.start();
		localTracker = new CallTrackerService();
		rmiSource.serverExpose(CallTracker.class.getSimpleName(), localTracker);
		remoteTracker = (CallTracker)rmiSource.clientLookup(CallTracker.class.getSimpleName());
	}
	
	@After
	public void teardownRmiSource() {
		rmiSource.stop();
	}
	
	@Test
	public void clientListingContainsExpectedName() {
		assertTrue(rmiSource.clientNames().contains(CallTracker.class.getSimpleName()));
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

}
