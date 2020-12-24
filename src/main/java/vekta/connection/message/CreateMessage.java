package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;
import vekta.sync.Syncable;
import vekta.world.GlobalOffset;

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
