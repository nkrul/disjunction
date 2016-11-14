package com.kncept.disjunction;

public class DisjunctionException extends RuntimeException {

	public DisjunctionException(String message) {
		super(message);
	}
	
	public DisjunctionException(Throwable cause) {
		super(cause);
	}
	
	public DisjunctionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
