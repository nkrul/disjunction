package koncept.disjunction.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaRmiSource implements RmiSource {
	private static final int defaultPort = 1099;
	private Registry serverRegistry;
	private Registry clientRegistry;
	private TrackedRMIServerSocketFactory ssf;
	private TrackedRMIClientSocketFactory csf;
	@Override
	public void start() {
		ssf = new TrackedRMIServerSocketFactory();
		csf = new TrackedRMIClientSocketFactory();
		try {
			serverRegistry = LocateRegistry.createRegistry(defaultPort, csf, ssf);
			clientRegistry = LocateRegistry.getRegistry("localhost", defaultPort, csf);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		
	}
	@Override
	public void stop() {
		if (serverRegistry != null) try {
			for(String name: serverRegistry.list())
				serverRegistry.unbind(name);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		if (ssf != null)
			ssf.close();
		
		serverRegistry = null;
		clientRegistry = null;
		ssf = null;
		csf = null;
		
	}
	@Override
	public List<String> clientNames() {
		try {
			return asList(clientRegistry.list());
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public Object clientLookup(String name) {
		try {
			return clientRegistry.lookup(name);
		} catch (RemoteException | NotBoundException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void serverExpose(String name, Object object) {
		try {
			Remote remoteObjectRef = UnicastRemoteObject.exportObject((Remote)object, 0, csf, ssf);
			serverRegistry.rebind(name, (Remote)remoteObjectRef);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private static class TrackedRMIServerSocketFactory implements RMIServerSocketFactory {
		private final List<ServerSocket> serverSockets = Collections.synchronizedList(new ArrayList<ServerSocket>());

		@Override
		public ServerSocket createServerSocket(int port) throws IOException {
			ServerSocket serverSocket = new ServerSocket(port);
			serverSockets.add(serverSocket);
			return serverSocket;
		}
		
		public void close() {
			for(ServerSocket socket: new ArrayList<>(serverSockets)) try {
				if (!socket.isClosed())
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				serverSockets.remove(socket);
			}
			
		}
	}
	
	private static class TrackedRMIClientSocketFactory implements RMIClientSocketFactory, Serializable { //interface bug: RMIClientSocketFactory SHOULD extend Serializable anyway
		@Override
		public Socket createSocket(String host, int port) throws IOException {
			return new Socket(host, port);
		}		
	}
	
}