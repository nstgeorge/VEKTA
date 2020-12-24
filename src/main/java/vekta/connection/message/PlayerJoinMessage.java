package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;
import vekta.player.Player;

public class PlayerJoinMessage implements Message {
	private final Player player;

	public PlayerJoinMessage(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onPlayerJoin(peer, this);
	}
}
