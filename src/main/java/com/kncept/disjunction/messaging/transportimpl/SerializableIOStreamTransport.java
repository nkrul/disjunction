package com.kncept.disjunction.messaging.transportimpl;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.kncept.disjunction.describe.IdentifiedMethodEvent;
import com.kncept.disjunction.describe.MethodEvent;
import com.kncept.disjunction.messaging.DisjunctMessageTransport;

public class SerializableIOStreamTransport<R extends MethodEvent, W extends MethodEvent> implements DisjunctMessageTransport<R, W> {
	private volatile boolean closed;
	
	private final InputStream in;
	private ObjectInputStream oIn;
	private final OutputStream out;
	private ObjectOutputStream oOut;
	
	private final Closeable streamSource;
	
	public SerializableIOStreamTransport(InputStream in, OutputStream out) throws IOException {
		streamSource = null;
		this.in = in;
		this.out = out;
	}
	
	public SerializableIOStreamTransport(Closeable streamSource, InputStream in, OutputStream out) throws IOException {
		this.streamSource = streamSource;
		this.in = in;
		this.out = out;
	}
	
	private ObjectInputStream in() throws IOException {
		if (oIn == null)
			oIn = new ObjectInputStream(in);
		return oIn;
	}
	
	private ObjectOutputStream out() throws IOException {
		if (oOut == null)
			oOut = new ObjectOutputStream(out);
		return oOut;
	}
	
	
	@Override
	public void close() {
		closed = true;
		try {
			if (oIn == null) 
				in.close();
			else
				oIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (oOut == null)
				out.close();
			else
				oOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (streamSource != null) try {
			streamSource.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isClosed() {
		return closed;
	}
	
	public IdentifiedMethodEvent<R> read() throws IOException {
		if (closed) return null;
		try {
			return (IdentifiedMethodEvent)in().readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to deserialize", e);
		}
	};
	
	public void write(IdentifiedMethodEvent<W> message) throws IOException {
		if (closed) return;
		out().writeObject(message);
	};
}
