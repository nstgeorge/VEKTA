package vekta.connection.message;

import vekta.Syncable;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class AddMessage implements Message {
	private final Syncable object;

	public AddMessage(Syncable object) {
		this.object = object;
	}

	public Syncable getObject() {
		return object;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onAdd(peer, this);
	}
}
