package com.kncept.disjunction.test;

import java.util.List;

public interface RmiSource {
	public void start();
	public void stop();
	public List<String> clientNames();
	public Object clientLookup(String name);
	public void serverExpose(String name, Object object);
}
