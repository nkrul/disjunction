package koncept.disjunction.loopback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import koncept.disjunction.DisjunctionRegistry;
import koncept.disjunction.object.ObjectSource;

public class DirectLoopbackRegistry implements DisjunctionRegistry {
	
	private final ObjectSource objects;
	private Map<String, Object> proxies = new LinkedHashMap<>();
	
	public DirectLoopbackRegistry(ObjectSource objects) {
		this.objects = objects;
	}
	
	@Override
	public List<String> list() {
		Collection<String> names = objects.names();
		if (names instanceof List) 
			return (List) names;
		return new ArrayList<String>(names);
	}
	
	@Override
	public Object lookup(String name) {
		Object proxy = proxies.get(name);
		if (proxy == null) {
			Object object = objects.get(name);
			if (object != null) {
				DirectInvocationHandler invoker = new DirectInvocationHandler(object);
				proxy = invoker.createProxy();
				proxies.put(name, proxy);
			}
		}
//		if (proxy == null && options.throwRMIExceptionOnNotFound)
//			throw new DisjunctionException("Not Found: " + name);
		return proxy;
	}
}
