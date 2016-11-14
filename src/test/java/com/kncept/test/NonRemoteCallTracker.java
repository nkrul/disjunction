package com.kncept.test;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import com.kncept.test.CallTracker.ThreadIdCallback;

public interface NonRemoteCallTracker {

	public Integer methodInvocationCount();
	
	public void checkedTestException() throws CheckedTestException;
	
	public String callback(ThreadIdCallback callable) throws Exception;
	
	public static class ThreadIdCallback implements Callable<String>, Serializable {
		@Override
		public String call() throws Exception {
			Thread thread = Thread.currentThread();
			return Long.toString(thread.getId());
		}
	}
	
}
