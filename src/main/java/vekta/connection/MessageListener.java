package vekta.connection;

import vekta.Faction;

import java.io.Serializable;

public interface MessageListener {
	default void onPlayerFaction(Peer peer, Faction faction) {
	}

	default void onSync(Peer peer, String key, Serializable data) {
	}
}
