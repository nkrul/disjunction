package koncept.disjunction.messaging;

import koncept.disjunction.describe.MethodInvocation;
import koncept.disjunction.describe.MethodResult;

public interface DisjunctMessaging {
	
	public MethodResult invocation(String name, MethodInvocation method);
	

}
