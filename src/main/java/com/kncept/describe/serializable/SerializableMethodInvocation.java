package com.kncept.describe.serializable;

import java.io.Serializable;

import com.kncept.describe.MethodInvocation;

public class SerializableMethodInvocation implements MethodInvocation, Serializable {
	
	private final String objectName;
	private final String methodSignature;
	private final Serializable[] args;
	
	public SerializableMethodInvocation(String objectName, String methodSignature, Serializable[] args) {
		this.objectName = objectName;
		this.methodSignature = methodSignature;
		this.args = args;
	}
	
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

}
