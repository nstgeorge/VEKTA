package vekta.connection;

import vekta.connection.message.Message;

public interface ConnectionListener extends MessageListener {
	default void onConnect() {
	}

	default void onDisconnect() {
	}

	default void onConnect(Peer peer) {
	}

	default void onDisconnect(Peer peer) {
	}
	
	default void onMessage(Peer peer, Message message) {
	}
}
