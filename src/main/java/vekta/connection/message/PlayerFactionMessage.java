package vekta.connection.message;

import vekta.Faction;
import vekta.connection.MessageListener;
import vekta.connection.Peer;

public class PlayerFactionMessage implements Message {
	private final Faction faction;

	public PlayerFactionMessage(Faction faction) {
		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onPlayerFaction(peer, this);
	}
}
