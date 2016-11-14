package com.kncept.invocation;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.kncept.describe.MethodIdentifier;
import com.kncept.describe.MethodResult;
import com.kncept.describe.serializable.SerializableMethodInvocation;
import com.kncept.messaging.ClientTransportSerializer;

/**
 * This class turns an invocation into a Disjunct Message
 * @author koncept
 *
 */
public class DisjunctInvocationToMessage implements InvocationHandler {

	private final ClientTransportSerializer messaging;
	private final String name;
	
	public DisjunctInvocationToMessage(String name, ClientTransportSerializer messaging) {
		this.name = name;
		this.messaging = messaging;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Serializable[] serializableArgs = new Serializable[args == null ? 0 : args.length];
		for(int i = 0; i < serializableArgs.length; i++)
			if (args[i] == null || args[i] instanceof Serializable)
				serializableArgs[i] = (Serializable)args[i];
			else throw new NotSerializableException("Argument index " + i + " not Serializable");
		SerializableMethodInvocation invocation = new SerializableMethodInvocation(
				name,
				MethodIdentifier.methodIdentifier(method),
				serializableArgs);
		
		MethodResult result = messaging.send(invocation);
		if (result.isException())
			throw (Throwable)result.value();
		return result.value();
	}

}
