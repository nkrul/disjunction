package com.kncept.disjunction.messaging;

import java.io.IOException;

import com.kncept.disjunction.describe.IdentifiedMethodEvent;
import com.kncept.disjunction.describe.MethodEvent;

public interface DisjunctMessageTransport <R extends MethodEvent, W extends MethodEvent> {

	public IdentifiedMethodEvent<R> read() throws IOException;
	public void write(IdentifiedMethodEvent<W> result) throws IOException;
	
	public void close();
	public boolean isClosed();
	
}
