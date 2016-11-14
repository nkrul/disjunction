package com.kncept.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kncept.describe.IdentifiedMethodEvent;
import com.kncept.describe.MethodInvocation;
import com.kncept.describe.MethodResult;
import com.kncept.invocation.DisjunctMessageToInvocation;
import com.kncept.messaging.DisjunctMessageTransport;
import com.kncept.object.ExposableObjectSource;
import com.kncept.object.ObjectSource;
import com.kncept.policy.BehaviourPolicy;
import com.kncept.service.internal.ServerImplicitCommands;

public class DisjunctionServer {
	private BehaviourPolicy options = new BehaviourPolicy();
	private final DisjunctMessageToInvocation invoker = new DisjunctMessageToInvocation();
	
	private final List<MessageReader> transportReaders = new ArrayList<>();
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	private final ObjectSource objects;
	
	public DisjunctionServer() {
		this(new ExposableObjectSource());
	}
	
	public DisjunctionServer(ObjectSource objects) {
		this.objects = objects;
	}
	
	public ObjectSource getObjects() {
		return objects;
	}
	
	//a list of what we are serving
	public List<String> list() {
		Collection<String> names = objects.names();
		if (names instanceof List) return (List)names;
		return new ArrayList<>(names);
	}
	
	public void expose(String name, Object object) {
		objects.add(name, object);
	}
	
	public void addTransport(DisjunctMessageTransport<MethodInvocation, MethodResult> transport) {
		MessageReader messageReader = new MessageReader(transport);
		transportReaders.add(messageReader);
		executor.submit(messageReader);
	}
	
	
	private class MessageReader implements Runnable {
		private final DisjunctMessageTransport<MethodInvocation, MethodResult> transport;
		private volatile boolean running = true;
		public MessageReader(DisjunctMessageTransport<MethodInvocation, MethodResult> transport) {
			this.transport = transport;
		}
		@Override
		public void run() {
			while(running) try {
				readAndProcessMessage();
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}
			close();
		}
		
		public DisjunctMessageTransport<MethodInvocation, MethodResult> transport() {
			return transport;
		}
		
		public void close() {
			running = false;
			transport.close();
		}
		
		private void readAndProcessMessage() throws IOException {
			IdentifiedMethodEvent<MethodInvocation> identifiedInvocation = transport.read();
			MethodInvocation invocation = identifiedInvocation.methodEvent();
			Object object = null;
			if (invocation.objectName() == null) object = new ServerImplicitCommandsImpl();
			else object = objects.get(invocation.objectName());
			if (object != null) {
				MethodResult result = invoker.invocation(object, invocation);
				transport.write(IdentifiedMethodEvent.identified(identifiedInvocation.id(), result));
			} else {
				MethodResult notFound = MethodResult.exception(new RuntimeException("Not Found"));
				transport.write(IdentifiedMethodEvent.identified(identifiedInvocation.id(), notFound));
			}
			
		}
	}
	
	public class ServerImplicitCommandsImpl implements ServerImplicitCommands {
		@Override
		public String[] names() {
			return objects.names().toArray(new String[]{});
		}
		
		@Override
		public String[] interfaces(String name) {
			Object object = objects.get(name);
			if (object != null) {
				Class<?>[] ifClasses = object.getClass().getInterfaces();
				String[] ifNames = new String[ifClasses.length];
				for(int i = 0; i < ifClasses.length; i++) {
					ifNames[i] = ifClasses[i].getCanonicalName();
				}
				return ifNames;
			}
			return null;
		}
	}
}
