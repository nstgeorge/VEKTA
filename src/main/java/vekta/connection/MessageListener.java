package vekta.connection;

import vekta.connection.message.*;

public interface MessageListener {
	default void onPlayerJoin(Peer peer, PlayerJoinMessage msg) {
	}

	default void onSyncObject(Peer peer, SyncMessage msg) {
	}

	default void onRequestObject(Peer peer, RequestMessage msg) {
	}

	default void onCreateObject(Peer peer, CreateMessage msg) {
	}

	default void onMoveObject(Peer peer, MoveMessage msg) {
	}

	default void onChangeRenderLevel(Peer peer, RenderLevelMessage msg) {
	}
}
