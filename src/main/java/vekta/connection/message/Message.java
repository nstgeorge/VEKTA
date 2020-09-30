package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;

import java.io.Serializable;

public interface Message extends Serializable {
	void receive(Peer peer, MessageListener listener);
}
