package koncept.disjunction;

import java.io.NotSerializableException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import koncept.disjunction.describe.MethodIdentifier;
import koncept.disjunction.describe.MethodInvocation;
import koncept.disjunction.describe.MethodResult;
import koncept.disjunction.describe.SerializableMethodResult;
import koncept.disjunction.invoke.DisjunctMethodInvocation;
import koncept.disjunction.messaging.DisjunctMessaging;
import koncept.disjunction.policy.BehaviourPolicy;

public class DisjunctionServer implements DisjunctionRegistry {
	private BehaviourPolicy options = new BehaviourPolicy();
	private DisjunctMessaging localMessaging = new LoopbackMessaging();
	
	private Map<String, Object> originals = new LinkedHashMap<>();
	private Map<String, Object> proxies = new LinkedHashMap<>();
	
	@Override
	public List<String> list() {
		return new ArrayList<>(originals.keySet());
	}
	
	@Override
	public Object lookup(String name) {
		return proxies.get(name);
	}
	
	public void expose(String name, Object object) {
		Object proxy = Proxy.newProxyInstance(
				object.getClass().getClassLoader(),
				object.getClass().getInterfaces(),
				new DisjunctMethodInvocation(name, localMessaging));
		originals.put(name, object);
		proxies.put(name, proxy);
	}
	
	private class LoopbackMessaging implements DisjunctMessaging {
		@Override
		public MethodResult invocation(String name, MethodInvocation method) {
			Object original = originals.get(name);
			if (original == null) throw new RuntimeException(new NotBoundException(name));
			try {
				Method reflection = lookupMethod(original, method.methodSignature());
				
				Object returnValue = reflection.invoke(original, method.args());
				return new SerializableMethodResult(returnValue, false);
			} catch (Exception e) {
				try {
					return new SerializableMethodResult(e, true);
				} catch (NotSerializableException e1) {
					throw new RuntimeException(e1);
				}
			}
		}
		
		private Method lookupMethod(Object object, String methodSignature) throws NoSuchMethodException {
			for(Method method: object.getClass().getMethods())
				if (MethodIdentifier.methodIdentifier(method).equals(methodSignature))
					return method;
			throw new NoSuchMethodException("No such method: " + methodSignature);
		}
	}
}
