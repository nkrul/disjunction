package com.kncept.test;

import java.rmi.RemoteException;
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
	public String callback(ThreadIdCallback callable) throws Exception {
		return callable.call();
	}

}
