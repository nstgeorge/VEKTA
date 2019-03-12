package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class TimeScaleMessage implements Message {
	private final float timeScale;

	public TimeScaleMessage(float timeScale) {
		this.timeScale = timeScale;
	}

	public float getTimeScale() {
		return timeScale;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onChangeRenderLevel(peer, this);
	}
}
