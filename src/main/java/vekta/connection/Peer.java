package vekta.connection;

/**
 * High-level abstraction for multiplayer peer
 */
public class Peer {
	private final String id;

	public Peer(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}
}
