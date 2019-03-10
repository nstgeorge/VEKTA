package vekta.connection;

import io.socket.client.IO;
import io.socket.client.Socket;
import vekta.connection.message.Message;

import java.util.*;
import java.util.stream.Collectors;

import static processing.core.PApplet.println;

/**
 * High-level abstraction for external game server connection
 */
public class Connection {
	private final Socket socket;

	private final List<ConnectionListener> listeners = new ArrayList<>();
	private final Map<String, Peer> peers = new HashMap<>();

	public Connection(String address) {
		try {
			socket = IO.socket(address);

			// Socket configuration
			socket.on(Socket.EVENT_CONNECT, args -> {
				println("Connected to " + address);
				for(ConnectionListener listener : listeners) {
					listener.onConnect();
				}
			});
			socket.on(Socket.EVENT_DISCONNECT, args -> {
				for(ConnectionListener listener : listeners) {
					listener.onDisconnect();
				}
			});
			socket.on(Socket.EVENT_ERROR, args -> {
				String text = Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", "));
				println("Error: " + text);
			});

			// Protocol configuration
			socket.on("leave", args -> {
				String id = String.valueOf(args[0]);
				Peer peer = peers.remove(id);
				if(peer != null) {
					for(ConnectionListener listener : listeners) {
						listener.onDisconnect(peer);
					}
				}
			});
			socket.on("msg", args -> {
				try {
					String id = String.valueOf(args[0]);
					Message message = (Message)args[1];
					Peer peer = getPeer(id);
					for(ConnectionListener listener : listeners) {
						listener.onMessage(message);
						message.receive(peer, listener);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					println("Received invalid packet: " + e.getMessage());
				}
			});
			socket.connect();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to connect to multiplayer server", e);
		}
	}

	private Peer getPeer(String id) {
		Peer peer = peers.get(id);
		if(peer == null) {
			peer = new Peer(id);
			peers.put(id, peer);
			for(ConnectionListener listener : listeners) {
				listener.onConnect(peer);
			}
		}
		return peer;
	}

	public void addListener(ConnectionListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ConnectionListener listener) {
		listeners.remove(listener);
	}

	public void joinRoom(String room) {
		socket.emit("join", new Object[] {room}, a -> println("Joined room: " + room));
	}

	//	public void leaveRoom() {
	//		socket.emit("leave", new Object[] {}, a -> println("Left room"));
	//	}

	public void send(Message message) {
		socket.send("msg", message);
	}

	public void close() {
		socket.close();
		for(ConnectionListener listener : listeners) {
			listener.onDisconnect();
		}
		println("Connection closed");
	}
}
