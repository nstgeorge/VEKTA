package vekta.connection.message;

import processing.core.PVector;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class MoveMessage implements Message {
	private final String key;
	private final double x, y;
	private final PVector velocity;
	private final long timestamp;

	public MoveMessage(String key, double x, double y, PVector velocity, long timestamp) {
		this.key = key;
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.timestamp = timestamp;
	}

	public String getKey() {
		return key;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public PVector getVelocity() {
		return velocity;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onObjectMove(peer, this);
	}
}
