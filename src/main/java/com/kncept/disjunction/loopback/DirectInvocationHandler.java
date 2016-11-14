package com.kncept.disjunction.loopback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The most basic type of proxy there is.<br>
 * the JVM turns this a method call into reflection objects, which are then directly executed by the Invocation Handler
 * @author koncept
 *
 */
public class DirectInvocationHandler implements InvocationHandler {
	
	private final Object object;
	
	public DirectInvocationHandler(Object object) {
		this.object = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(object, args);
	}
	
	public Object getObject() {
		return object;
	}
	
	public Object createProxy() {
		return Proxy.newProxyInstance(
				object.getClass().getClassLoader(),
				object.getClass().getInterfaces(),
				this);
	}

}
