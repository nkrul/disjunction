package com.kncept.invocation;

import java.io.NotSerializableException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import com.kncept.describe.MethodIdentifier;
import com.kncept.describe.MethodInvocation;
import com.kncept.describe.MethodResult;
import com.kncept.describe.serializable.SerializableMethodResult;

public class DisjunctMessageToInvocation {
	private static final Logger logger = Logger.getLogger(DisjunctMessageToInvocation.class.getName());

	public MethodResult invocation(Object object, MethodInvocation method) {
		try {
			try {
				Method reflection = lookupMethod(object, method.methodSignature());
				Object returnValue = reflection.invoke(object, method.args());
				return new SerializableMethodResult(returnValue, false);
			} catch (NoSuchMethodException e) {
				return new SerializableMethodResult(e, true);
			}  catch (IllegalAccessException | IllegalArgumentException e) {
				return new SerializableMethodResult(e, true);
			} catch (InvocationTargetException e) {
				if (e.getCause() != null)
					return new SerializableMethodResult(e.getCause(), true);
				return new SerializableMethodResult(e, true);
			}
		} catch (NotSerializableException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Method lookupMethod(Object object, String methodSignature) throws NoSuchMethodException {
		for(Method method: object.getClass().getMethods())
			if (MethodIdentifier.methodIdentifier(method).equals(methodSignature))
				return method;
		throw new NoSuchMethodException("No such method: " + methodSignature);
	}
	
}
