package koncept.disjunction.invoke;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import koncept.disjunction.describe.MethodIdentifier;
import koncept.disjunction.describe.MethodResult;
import koncept.disjunction.describe.SerializableMethodInvocation;
import koncept.disjunction.messaging.DisjunctMessaging;

public class DisjunctMethodInvocation implements InvocationHandler {

	private final DisjunctMessaging messaging;
	private final String name;
	
	public DisjunctMethodInvocation(String name, DisjunctMessaging messaging) {
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
		SerializableMethodInvocation invocation = new SerializableMethodInvocation(MethodIdentifier.methodIdentifier(method), serializableArgs);
		
		MethodResult result = messaging.invocation(name, invocation);
		if (result.isException())
			throw (Throwable)result.value();
		return result.value();
	}

}
