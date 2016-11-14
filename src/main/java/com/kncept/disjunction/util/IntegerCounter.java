package com.kncept.disjunction.util;

public class IntegerCounter {

	private final int min;
	private final int max;
	private int value;
	
	public IntegerCounter() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public IntegerCounter(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public synchronized int next() {
		int current = value;
		if (value == max)
			value = min;
		else
			value++;
		return current;
	}
	
}
