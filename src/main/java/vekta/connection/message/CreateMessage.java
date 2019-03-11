package vekta.connection.message;

import vekta.GlobalOffset;
import vekta.Syncable;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class CreateMessage implements Message {
	private final Syncable object;
	private final GlobalOffset offset;

	public CreateMessage(Syncable object, GlobalOffset offset) {
		this.object = object;
		this.offset = offset;
	}

	public Syncable getObject() {
		return object;
	}

	public GlobalOffset getOffset() {
		return offset;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onCreateObject(peer, this);
	}
}
