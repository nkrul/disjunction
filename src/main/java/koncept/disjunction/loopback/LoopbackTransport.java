package koncept.disjunction.loopback;

import java.util.concurrent.ConcurrentLinkedQueue;

import koncept.disjunction.describe.IdentifiedMethodEvent;
import koncept.disjunction.describe.MethodEvent;
import koncept.disjunction.messaging.DisjunctMessageTransport;

public class LoopbackTransport<R extends MethodEvent, W extends MethodEvent> implements DisjunctMessageTransport<R, W> {
	private final LoopbackTransport<W, R> looped;
	private final ConcurrentLinkedQueue<IdentifiedMethodEvent<R>> readQueue = new ConcurrentLinkedQueue<>();
	private volatile boolean closed;
	
	public LoopbackTransport() {
		this.looped = new LoopbackTransport<>(this);
	}
	private LoopbackTransport(LoopbackTransport<W, R> looped) {
		this.looped = looped;
	}
	
	public LoopbackTransport<W, R> looped() {
		return looped;
	}
	
	@Override
	public void close() {
		closed = true;
		looped.closed = true;
	}
	
	@Override
	public boolean isClosed() {
		return closed;
	}
	
	public IdentifiedMethodEvent<R> read() {
		while (readQueue.isEmpty() && !closed) {
			try {
				Thread.sleep(10);
			} catch(InterruptedException e) {
				
			}
		}
		return readQueue.poll();
	};
	
	public void write(IdentifiedMethodEvent<W> message) {
		looped.readQueue.offer(message);
	};

}
