package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;

import java.io.Serializable;

public class ObjectUnknownMessage implements Message {
	private final String key;

	public ObjectUnknownMessage(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onObjectRequest(peer, getKey());
	}
}
