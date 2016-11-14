package com.kncept.test;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

public interface CallTracker extends Remote {

	public Integer methodInvocationCount() throws RemoteException;
	
	public void checkedTestException() throws CheckedTestException, RemoteException;
	
	public String callback(ThreadIdCallback callable) throws Exception;
	
	public static class ThreadIdCallback implements Callable<String>, Serializable {
		@Override
		public String call() throws Exception {
			Thread thread = Thread.currentThread();
			return Long.toString(thread.getId());
		}
	}
}
