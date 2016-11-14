package com.kncept.disjunction.test;

import java.util.List;

import com.kncept.disjunction.describe.MethodInvocation;
import com.kncept.disjunction.describe.MethodResult;
import com.kncept.disjunction.loopback.LoopbackTransport;
import com.kncept.disjunction.service.DisjunctionClient;
import com.kncept.disjunction.service.DisjunctionServer;

public class LoopbackRmiSource implements RmiSource {
	private final DisjunctionServer server;
	private final DisjunctionClient client;
	public LoopbackRmiSource() {
		server = new DisjunctionServer();
		LoopbackTransport<MethodInvocation, MethodResult> loopback = new LoopbackTransport<MethodInvocation, MethodResult>();
		server.addTransport(loopback);
		client = new DisjunctionClient(loopback.looped());
	}
	@Override
	public void start() {
	}
	@Override
	public void stop() {
	}
	@Override
	public List<String> clientNames() {
		return client.list();
	}
	@Override
	public Object clientLookup(String name) {
		return client.lookup(name);
	}
	@Override
	public void serverExpose(String name, Object object) {
		server.expose(name, object);
	}
}