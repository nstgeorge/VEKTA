package vekta.connection.message;

import vekta.RenderLevel;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class RenderLevelMessage implements Message {
	private final RenderLevel level;

	public RenderLevelMessage(RenderLevel level) {
		this.level=level;
	}

	public RenderLevel getLevel() {
		return level;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onChangeRenderLevel(peer, this);
	}
}
