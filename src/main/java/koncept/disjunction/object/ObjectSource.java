package koncept.disjunction.object;

import java.util.Collection;

import koncept.disjunction.DisjunctionException;


/**
 * This represents a source of names and their associated objects.<br>
 * <br>
 * One example is that this can be all the objects that have been shared manually (similar to Java RMI)<br>
 * Another example is this could wrap a Spring Bean Factory or Application Context
 * 
 * @author koncept
 *
 */
public interface ObjectSource {

	/**
	 * This must return an empty collection when no names exist
	 * @return
	 */
	public Collection<String> names();
	
	/**
	 * This must  return null when a name does not exist
	 * @param name
	 * @return
	 */
	public Object get(String name);
	
	/**
	 * This should either add (or overwrite) an *original* object with the said name, or 
	 * throw an exception if adding is not permitted.<br>
	 * 
	 * @param name
	 * @param object
	 * @throws DisjunctionException if adding id not allowed
	 */
	public void add(String name, Object object) throws DisjunctionException;
	
}
