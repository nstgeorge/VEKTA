package vekta.connection;

import vekta.Faction;

public interface MessageListener {
	default void onPlayerFaction(Peer peer, Faction faction) {
	}
}
