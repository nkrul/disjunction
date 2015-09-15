package koncept.disjunction.describe;

import java.io.Serializable;

public class SerializableMethodInvocation implements MethodInvocation, Serializable {
	
	private final String methodSignature;
	private final Serializable[] args;
	
	public SerializableMethodInvocation(String methodSignature, Serializable[] args) {
		this.methodSignature = methodSignature;
		this.args = args;
	}

	@Override
	public String methodSignature() {
		return methodSignature;
	}
	
	@Override
	public Object[] args() {
		return args;
	}

}
