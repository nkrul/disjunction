package com.kncept.disjunction.describe;


public interface MethodInvocation extends MethodEvent {

	public String objectName(); //used to look up the object
	
	public String methodSignature();
	
	public Object[] args();
	
	public static MethodInvocation invocation(final String objectName, final String methodSignature, final Object[] args) {
		return new MethodInvocation() {
			@Override
			public String objectName() {
				return objectName;
			}
			@Override
			public String methodSignature() {
				return methodSignature;
			}
			@Override
			public Object[] args() {
				return args;
			}
		};
	}
	
}
