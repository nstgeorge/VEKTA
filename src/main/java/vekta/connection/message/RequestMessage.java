package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class RequestMessage implements Message {
	private final long id;

	public RequestMessage(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onRequestObject(peer, this);
	}
}
