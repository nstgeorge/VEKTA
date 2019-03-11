package vekta.connection.message;

import vekta.Syncable;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class ObjectMessage implements Message {
	private final Syncable object;

	public ObjectMessage(Syncable object) {
		this.object = object;
	}

	public Syncable getObject() {
		return object;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onObject(peer, getObject());
	}
}
