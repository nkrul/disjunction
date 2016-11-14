package com.kncept.disjunction.describe;

public interface MethodResult extends MethodEvent {

	public Object value();
	
	public boolean isException();
	
	public static MethodResult value(final Object value) {
		return new MethodResult() {
			@Override
			public Object value() {
				return value;
			}
			@Override
			public boolean isException() {
				return false;
			}
		};
	}
	
	public static MethodResult exception(final Throwable throwable) {
		return new MethodResult() {
			@Override
			public Object value() {
				return throwable;
			}
			@Override
			public boolean isException() {
				return true;
			}
		};
	}
	
}
