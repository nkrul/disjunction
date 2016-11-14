package com.kncept.service;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.kncept.describe.MethodInvocation;
import com.kncept.describe.MethodResult;
import com.kncept.invocation.DisjunctInvocationToMessage;
import com.kncept.messaging.ClientTransportSerializer;
import com.kncept.messaging.DisjunctMessageTransport;
import com.kncept.service.internal.ServerImplicitCommands;

public class DisjunctionClient {
	private final ClientTransportSerializer transportSerializer;
	private final ServerImplicitCommands server;
	
	public DisjunctionClient(DisjunctMessageTransport<MethodResult, MethodInvocation> transport) {
		transportSerializer = new ClientTransportSerializer(transport);
		
		server = (ServerImplicitCommands)newProxyInstance(
				 getClass().getClassLoader(),
				 new Class[]{ServerImplicitCommands.class},
				 new DisjunctInvocationToMessage(null, transportSerializer));
	}

//	@Override
	public List<String> list() {
		return new ArrayList<String>(asList(server.names()));
	}
	
//	@Override
	public Object lookup(String name) {
		try {
			String[] ifNames = server.interfaces(name);
			if (ifNames == null) throw new RuntimeException("No object to lookup: " + name);
			
			Class<?>[] ifClasses = new Class[ifNames.length];
			for(int i = 0; i < ifNames.length; i++)
				ifClasses[i] = Class.forName(ifNames[i]);
			
			return newProxyInstance(
					 getClass().getClassLoader(),
					 ifClasses,
					 new DisjunctInvocationToMessage(name, transportSerializer));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
}
