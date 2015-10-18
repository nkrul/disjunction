package koncept.disjunction.test;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class CallTrackerService implements CallTracker {

	public final AtomicInteger methodInvocationCount = new AtomicInteger();
	public final AtomicInteger checkedTestException = new AtomicInteger();
	
	@Override
	public Integer methodInvocationCount() throws RemoteException {
		return methodInvocationCount.getAndIncrement();
	}
	
	@Override
	public void checkedTestException() throws CheckedTestException, RemoteException {
		checkedTestException.getAndIncrement();
		throw new CheckedTestException();
	}
	
	@Override
	public <V> V callback(Callable<V> callable) throws RemoteException {
		System.err.println("TODO: perform callback");
		return null;
	}

}
