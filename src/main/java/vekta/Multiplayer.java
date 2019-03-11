package vekta;

import processing.core.PVector;
import vekta.connection.Connection;
import vekta.connection.ConnectionListener;
import vekta.connection.Peer;
import vekta.connection.message.*;
import vekta.context.TextInputContext;
import vekta.object.SpaceObject;
import vekta.spawner.WorldGenerator;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import static vekta.Vekta.*;

public class Multiplayer extends Singleplayer implements ConnectionListener {
	private static final String SERVER_ADDRESS =
			Settings.getString("multiplayer.server", "http://vekta-rvanasa.c9users.io");
	private static final String DEFAULT_ROOM =
			Settings.getString("multiplayer.room", "Test");

	private static final int PLAYER_POSITION_FRAMES = 10;

	private final transient Map<Peer, Faction> playerFactions = new WeakHashMap<>();

	private transient Connection connection;

	@Override
	public float getTimeScale() {
		return 1;
	}

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
			send(new RegisterMessage(getPlayer().getShip()));

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
			//				other.send(new RegisterMessage(new PirateShip("Yarr", PVector.random2D(), new PVector(-500, -500), new PVector(), WorldGenerator.randomPlanetColor())));
			//			}).start();
		}));
		switchContext();
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

	//// Connection listeners

	@Override
	public void onDisconnect(Peer peer) {
		Faction faction = playerFactions.remove(peer);
		if(faction != null) {
			getPlayer().send(faction.getName() + " left the world");
		}
	}

	//// Message listeners

	@Override
	public void onPlayerFaction(Peer peer, PlayerFactionMessage msg) {
		boolean existed = playerFactions.containsKey(peer);
		Faction faction = msg.getFaction();
		playerFactions.put(peer, register(faction));
		if(!existed) {
			getPlayer().send(faction.getName() + " joined the world");
			peer.send(new PlayerFactionMessage(getPlayer().getFaction()));
			peer.send(new RegisterMessage(getPlayer().getShip()));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onSync(Peer peer, SyncMessage msg) {
		String key = msg.getKey();
		println("<receive>", key);
		Syncable object = state.getSyncable(key);
		if(object != null) {
			object.onSync(msg.getData());
		}
		else {
			peer.send(new RequestMessage(key));
		}
	}

	@Override
	public void onRequest(Peer peer, RequestMessage msg) {
		Syncable object = state.getSyncable(msg.getKey());
		if(object != null) {
			peer.send(new RegisterMessage(object));
		}
	}

	@Override
	public void onRegister(Peer peer, RegisterMessage msg) {
		state.register(msg.getObject());
	}

	@Override
	public void onObjectMove(Peer peer, MoveMessage msg) {
		Syncable object = state.getSyncable(msg.getKey()); // TODO: lookup by SpaceObject ID
		if(object instanceof SpaceObject) {
			SpaceObject s = ((SpaceObject)object);
			PVector pos = state.getLocalPosition(msg.getX(), msg.getY());
			PVector vel = state.getLocalVelocity(msg.getVelocity());
			s.getPositionReference().set(pos);
			s.setVelocity(vel);
			s.simulateForward((int)(System.currentTimeMillis() - msg.getTimestamp()));
		}
	}

	//// World methods

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
	public void cleanup() {
		super.cleanup();

		if(connection != null) {
			connection.close();
		}
	}

	@Override
	public void render() {
		super.render();

		if(v.frameCount % PLAYER_POSITION_FRAMES == 0) {
			PVector pos = getPlayerShip().getPosition();
			PVector vel = getPlayerShip().getVelocity();
			send(new MoveMessage(
					state.getKey(getPlayerShip()),
					state.getGlobalX(pos.x),
					state.getGlobalY(pos.y),
					state.getGlobalVelocity(vel),
					System.currentTimeMillis()));
		}
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
