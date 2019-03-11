package vekta.connection;

import vekta.Faction;
import vekta.Syncable;

import java.io.Serializable;

public interface MessageListener {
	default void onPlayerFaction(Peer peer, Faction faction) {
	}

	default void onSync(Peer peer, String key, Serializable data) {
	}

	default void onObjectRequest(Peer peer, String key) {
	}

	default void onObject(Peer peer, Syncable object) {
	}
}
