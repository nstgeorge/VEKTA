package vekta.connection;

import vekta.connection.message.*;

public interface MessageListener {
	default void onPlayerJoin(Peer peer, PlayerJoinMessage msg) {
	}

	default void onSync(Peer peer, SyncMessage msg) {
	}

	default void onRequest(Peer peer, RequestMessage msg) {
	}

	default void onAdd(Peer peer, AddMessage msg) {
	}

	default void onMove(Peer peer, MoveMessage msg) {
	}

	default void onRenderLevel(Peer peer, RenderLevelMessage msg) {
	}
}
