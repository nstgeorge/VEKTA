package vekta.connection.message;

import processing.core.PVector;
import vekta.connection.MessageListener;
import vekta.connection.Peer;
import vekta.object.SpaceObject;
import vekta.world.WorldState;

public class MoveMessage implements Message {
	private final long id;
	private final double x, y;
	private final PVector velocity;
	private final long timestamp;

	public MoveMessage(SpaceObject s, WorldState state) {
		this(s.getSyncID(),
				state.getGlobalX(s.getPositionReference().x),
				state.getGlobalY(s.getPositionReference().y),
				state.getGlobalVelocity(s.getVelocity()),
				System.currentTimeMillis());
	}

	public MoveMessage(long id, double x, double y, PVector velocity, long timestamp) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.timestamp = timestamp;
	}

	public long getID() {
		return id;
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
		listener.onMoveObject(peer, this);
	}
}
