package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;

import java.io.Serializable;

public class SyncMessage implements Message {
	private final String key;
	private final Serializable data;

	public SyncMessage(String key, Serializable data) {
		this.key = key;
		this.data = data;
	}

	public String getKey() {
		return key;
	}

	public Serializable getData() {
		return data;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onSync(peer, getKey(), getData());
	}
}
