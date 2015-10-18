package koncept.disjunction.object;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import koncept.disjunction.DisjunctionException;


public class ExposableObjectSource implements ObjectSource {
	
	private final Map<String, Object> objects = new LinkedHashMap<>();
	
	@Override
	public void add(String name, Object object) throws DisjunctionException {
		objects.put(name, object);
	}
	
	@Override
	public Object get(String name) {
		return objects.get(name);
	}
	
	@Override
	public Collection<String> names() {
		return objects.keySet();
	}

}
