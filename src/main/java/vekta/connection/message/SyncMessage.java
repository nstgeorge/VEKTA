package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;
import vekta.sync.Syncable;

import java.io.Serializable;

public class SyncMessage implements Message {
	private final long id;
	private final Serializable data;
	
	public SyncMessage(Syncable s) {
		this(s.getSyncID(), s.getSyncData());
	}

	public SyncMessage(long id, Serializable data) {
		this.id = id;
		this.data = data;
	}

	public long getID() {
		return id;
	}

	public Serializable getData() {
		return data;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onSyncObject(peer, this);
	}
}
