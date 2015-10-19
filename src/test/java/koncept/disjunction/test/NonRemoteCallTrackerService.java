package koncept.disjunction.test;

import java.util.concurrent.atomic.AtomicInteger;

public class NonRemoteCallTrackerService implements NonRemoteCallTracker {

	public final AtomicInteger methodInvocationCount = new AtomicInteger();
	public final AtomicInteger checkedTestException = new AtomicInteger();
	
	@Override
	public Integer methodInvocationCount() {
		return methodInvocationCount.getAndIncrement();
	}

	@Override
	public void checkedTestException() throws CheckedTestException {
		checkedTestException.getAndIncrement();
		throw new CheckedTestException();
	}

	@Override
	public String callback(ThreadIdCallback callable) throws Exception {
		return callable.call();
	}

}
