package vekta.connection;

import vekta.connection.message.Message;

/**
 * High-level abstraction for multiplayer peer
 */
public final class Peer {
	private final Connection connection;
	private final String id;

	public Peer(Connection connection, String id) {
		this.connection = connection;
		this.id = id;
	}

	protected String getID() {
		return id;
	}

	public Connection getConnection() {
		return connection;
	}

	public void send(Message message) {
		getConnection().send(this, message);
	}
}
