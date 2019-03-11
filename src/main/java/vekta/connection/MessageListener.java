package vekta.connection;

import vekta.connection.message.*;

public interface MessageListener {
	default void onPlayer(Peer peer, PlayerMessage msg) {
	}

	default void onSync(Peer peer, SyncMessage msg) {
	}

	default void onRequest(Peer peer, RequestMessage msg) {
	}

	default void onRegister(Peer peer, RegisterMessage msg) {
	}

	default void onObjectMove(Peer peer, MoveMessage msg) {
	}
}
