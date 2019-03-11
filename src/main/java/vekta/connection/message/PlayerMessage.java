package vekta.connection.message;

import vekta.Player;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class PlayerMessage implements Message {
	private final Player player;

	public PlayerMessage(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onPlayer(peer, this);
	}
}
