package koncept.disjunction;

import java.rmi.Remote;
import java.util.List;

public interface DisjunctionRegistry {

	public List<String> list();
	
	public Object lookup(String name);
	
	
}
