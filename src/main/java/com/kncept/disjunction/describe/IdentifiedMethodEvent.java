package com.kncept.disjunction.describe;

public interface IdentifiedMethodEvent<T extends MethodEvent> {

	public String id();
	
	public T methodEvent();
	
	public static <T extends MethodEvent> IdentifiedMethodEvent<T> identified(final String id, final T methodEvent) {
		return new IdentifiedMethodEvent<T>() {
			@Override
			public String id() {
				return id;
			}
			@Override
			public T methodEvent() {
				return methodEvent;
			}
		};
	}
	
	
}
