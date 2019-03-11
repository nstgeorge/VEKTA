package vekta;

import processing.core.PVector;
import vekta.connection.Connection;
import vekta.connection.ConnectionListener;
import vekta.connection.Peer;
import vekta.connection.message.*;
import vekta.context.TextInputContext;
import vekta.spawner.WorldGenerator;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.WeakHashMap;

import static vekta.Vekta.*;

public class Multiplayer extends Singleplayer implements ConnectionListener {
	private static final String SERVER_ADDRESS =
			Settings.getString("multiplayer.server", "http://vekta-rvanasa.c9users.io");
	private static final String DEFAULT_ROOM =
			Settings.getString("multiplayer.room", "Test");

	private final transient Map<Peer, Faction> playerFactions = new WeakHashMap<>();

	private transient Connection connection;
	
	@Override
	public void start() {
		connection = new Connection(SERVER_ADDRESS);
		connection.addListener(this);

		super.start();

		setContext(new TextInputContext(mainMenu, "Choose a Name:", Resources.generateString("person") /*Settings.getString("multiplayer.name", Resources.generateString("person"))*/, name -> {
			getPlayer().getFaction().setName(name.trim());
			getPlayer().getFaction().setColor(0xFF000000 & Integer.parseInt(Settings.getString("multiplayer.color", String.valueOf(WorldGenerator.randomPlanetColor()))));

			Settings.set("multiplayer.name", name);
			Settings.set("multiplayer.color", Integer.toHexString(0x00FFFFFF & getPlayer().getFaction().getColor()));

			getPlayer().getShip().getPositionReference().add(PVector.random2D().mult(v.random(200, 1000)));

			connection.joinRoom(DEFAULT_ROOM);
			setContext(this);

			send(new PlayerFactionMessage(getPlayer().getFaction()));
			send(new ObjectMessage(getPlayer().getShip()));

//			// Simulate another player
//			new Thread(() -> {
//				try {
//					Thread.sleep(100);
//				}
//				catch(InterruptedException ignored) {
//				}
//
//				// Simulate another player
//				Connection other = new Connection(SERVER_ADDRESS);
//				other.joinRoom(DEFAULT_ROOM);
//				other.send(new PlayerFactionMessage(new Faction(Resources.generateString("person"), WorldGenerator.randomPlanetColor())));
//				other.send(new ObjectMessage(new PirateShip("Yarr", PVector.random2D(), new PVector(-500, -500), new PVector(), WorldGenerator.randomPlanetColor())));
//			}).start();
		}));
		switchContext();
	}

	@Override
	public void cleanup() {
		super.cleanup();

		if(connection != null) {
			connection.close();
		}
	}

	public void send(Message message) {
		handleErrors(() -> connection.send(message),
				"Error occurred while broadcasting " + message.getClass().getSimpleName());
	}

	public void send(Peer peer, Message message) {
		handleErrors(() -> connection.send(peer, message),
				"Error occurred while sending " + message.getClass().getSimpleName());
	}

	private void handleErrors(Runnable runnable, String message) {
		try {
			runnable.run();
		}
		catch(Exception e) {
			//			setContext(mainMenu);
			e.printStackTrace();
			getPlayer().send(message)
					.withColor(DANGER_COLOR);
		}
	}

	@Override
	public void onPlayerFaction(Peer peer, Faction faction) {
		boolean existed = playerFactions.containsKey(peer);
		playerFactions.put(peer, faction);
		if(!existed) {
			getPlayer().send(faction.getName() + " joined the world");
			peer.send(new PlayerFactionMessage(getPlayer().getFaction()));
			peer.send(new ObjectMessage(getPlayer().getShip()));
		}
	}

	@Override
	public void onDisconnect(Peer peer) {
		Faction faction = playerFactions.remove(peer);
		if(faction != null) {
			getPlayer().send(faction.getName() + " left the world");
		}
	}

	@Override
	public <T extends Syncable> T register(T object) {
		object = super.register(object);
		apply(object);
		return object;
	}

	@Override
	public void apply(Syncable object) {
		if(object.shouldSync()) {
			println("<broadcast>", state.getKey(object));
			connection.send(new SyncMessage(state.getKey(object), object.getSyncData()));
		}

		super.apply(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onSync(Peer peer, String key, Serializable data) {
		println("<receive>", key);
		Syncable object = state.getSyncableObject(key);
		if(object != null) {
			object.onSync(data);
		}
		else {
			peer.send(new ObjectUnknownMessage(key));
		}
	}

	@Override
	public void onObjectRequest(Peer peer, String key) {
		Syncable object = state.getSyncableObject(key);
		if(object != null) {
			peer.send(new ObjectMessage(object));
		}
	}

	@Override
	public void onObject(Peer peer, Syncable object) {
		state.register(object);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void restart() {
		cleanup();

		setContext(new Multiplayer());
	}

	//	@Override
	//	public void keyPressed(KeyBinding key) {
	//		if(key == KeyBinding.QUICK_SAVE || key == KeyBinding.QUICK_LOAD) {
	//			return; // Prevent quick-saving/loading in multiplayer
	//		}
	//		super.keyPressed(key);
	//	}

	@Override
	public boolean load(File file) {
		return false;
	}

	@Override
	public boolean save(File file) {
		return false;
	}
}
