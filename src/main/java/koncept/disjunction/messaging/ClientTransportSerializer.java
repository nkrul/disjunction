package koncept.disjunction.messaging;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import koncept.disjunction.describe.IdentifiedMethodEvent;
import koncept.disjunction.describe.MethodInvocation;
import koncept.disjunction.describe.MethodResult;
import koncept.disjunction.util.IntegerCounter;

public class ClientTransportSerializer {

	private final DisjunctMessageTransport<MethodResult, MethodInvocation> transport;
	private final IntegerCounter counter = new IntegerCounter();
	private final ConcurrentHashMap<String, CompletableFuture<MethodResult>> results = new ConcurrentHashMap<>();
	
	public ClientTransportSerializer(DisjunctMessageTransport<MethodResult, MethodInvocation> transport) {
		this.transport = transport;
	}
	
	
	
	public MethodResult send(MethodInvocation invocation) throws IOException, InterruptedException, ExecutionException {
		String id = Integer.toString(counter.next());
		CompletableFuture<MethodResult> result = new CompletableFuture<MethodResult>();
		results.put(id, result);
		transport.write(IdentifiedMethodEvent.identified(id, invocation));
		update(); //try and fish for at least one message back before we block
		return result.get();
	}
	
	public void update() throws IOException {
		IdentifiedMethodEvent<MethodResult> result = transport.read();
		CompletableFuture<MethodResult> toComplete = results.get(result.id());
		toComplete.complete(result.methodEvent());
	}
	
}
