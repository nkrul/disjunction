package com.kncept.disjunction.describe;

import java.io.Serializable;

public interface IdentifiedMethodEvent<T extends MethodEvent> {

	public String id();
	
	public T methodEvent();
	
	public static <T extends MethodEvent> IdentifiedMethodEvent<T> identified(final String id, final T methodEvent) {
		
		if (Serializable.class.isAssignableFrom(methodEvent.getClass())) {
			return new SerializableImpl(id, (Serializable)methodEvent);
		}
		
		return new DefaultImpl<T>(id, methodEvent);
	}
	
	public static class DefaultImpl<T extends MethodEvent> implements IdentifiedMethodEvent<T> {
		private final String id;
		private final T methodEvent;
		
		public DefaultImpl(String id, T methodEvent) {
			this.id = id;
			this.methodEvent = methodEvent;
		}
		
		@Override
		public String id() {
			return id;
		}
		@Override
		public T methodEvent() {
			return methodEvent;
		}
	}
	
	public static class SerializableImpl<T extends MethodEvent> implements IdentifiedMethodEvent<T>, Serializable {
		private final String id;
		private final T methodEvent;
		
		public SerializableImpl(String id, Serializable methodEvent) {
			this.id = id;
			this.methodEvent = (T)methodEvent;
		}
		
		@Override
		public String id() {
			return id;
		}
		@Override
		public T methodEvent() {
			return methodEvent;
		}
	}
	
}
