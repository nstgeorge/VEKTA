package vekta.connection.message;

import vekta.Syncable;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class RegisterMessage implements Message {
	private final Syncable object;

	public RegisterMessage(Syncable object) {
		this.object = object;
	}

	public Syncable getObject() {
		return object;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onRegister(peer, this);
	}
}
