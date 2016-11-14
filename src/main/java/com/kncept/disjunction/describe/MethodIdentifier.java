package com.kncept.disjunction.describe;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class MethodIdentifier {
	
	public static String methodIdentifier(Method method) {
		StringBuilder methodIdentifier = new StringBuilder();
		
		methodIdentifier.append(method.getReturnType().getName());
		methodIdentifier.append(" ");
		
		methodIdentifier.append(method.getName());
		methodIdentifier.append("(");
		
		Parameter[] parameters = method.getParameters();
		if (parameters != null && parameters.length != 0) {
			for(Parameter parameter: parameters) {
				methodIdentifier.append(" ");
				methodIdentifier.append(parameter.getType().getName());
			}
		}
		
		methodIdentifier.append(" )");
		
		Class[] exceptionTypes = method.getExceptionTypes();
		if (exceptionTypes != null && exceptionTypes.length != 0) {
			methodIdentifier.append(" throws");
			
			for(Class exceptionType: exceptionTypes) {
				methodIdentifier.append(" ");
				methodIdentifier.append(exceptionType.getName());
			}
		}
		
		return methodIdentifier.toString();
	}
	
}
