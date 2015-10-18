package koncept.disjunction.test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

public interface CallTracker extends Remote {

	public Integer methodInvocationCount() throws RemoteException;
	
	public void checkedTestException() throws CheckedTestException, RemoteException;
	
	public <V> V callback(Callable<V> callable) throws RemoteException;
}
