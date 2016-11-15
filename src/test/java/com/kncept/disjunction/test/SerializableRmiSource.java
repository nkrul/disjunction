package com.kncept.disjunction.test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

import com.kncept.disjunction.describe.MethodInvocation;
import com.kncept.disjunction.describe.MethodResult;
import com.kncept.disjunction.messaging.transportimpl.SerializableIOStreamTransport;
import com.kncept.disjunction.service.DisjunctionClient;
import com.kncept.disjunction.service.DisjunctionServer;

public class SerializableRmiSource implements RmiSource {
	private final DisjunctionServer server;
	private final DisjunctionClient client;
	
	private final PipedOutputStream serverWriteChannel;
	private final PipedInputStream serverReadChannel;
	private final PipedOutputStream clientWriteChannel;
	private final PipedInputStream clientReadChannel;
	
	public SerializableRmiSource() throws IOException {
		serverWriteChannel = new PipedOutputStream();
		serverReadChannel = new PipedInputStream();
		clientWriteChannel = new PipedOutputStream(serverReadChannel);
		clientReadChannel = new PipedInputStream(serverWriteChannel);
		
		SerializableIOStreamTransport<MethodInvocation, MethodResult> serverTransport = new SerializableIOStreamTransport<MethodInvocation, MethodResult>(serverReadChannel, serverWriteChannel);
		SerializableIOStreamTransport<MethodResult, MethodInvocation> clientTransport = new SerializableIOStreamTransport<MethodResult, MethodInvocation>(clientReadChannel, clientWriteChannel);
		
		server = new DisjunctionServer();
		server.addTransport(serverTransport);
		client = new DisjunctionClient(clientTransport);
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