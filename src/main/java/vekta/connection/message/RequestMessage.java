package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class RequestMessage implements Message {
	private final String key;

	public RequestMessage(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onRequest(peer, this);
	}
}
