package vekta;

import processing.core.PVector;
import vekta.connection.Connection;
import vekta.connection.ConnectionListener;
import vekta.connection.Peer;
import vekta.connection.message.*;
import vekta.context.TextInputContext;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.object.ship.Ship;
import vekta.spawner.WorldGenerator;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import static vekta.Vekta.*;

public class Multiplayer extends Singleplayer implements ConnectionListener {
	private static final String SERVER_ADDRESS =
			Settings.getString("multiplayer.server", "http://vekta-rvanasa.c9users.io");
	private static final String DEFAULT_ROOM =
			Settings.getString("multiplayer.room", "Test");

	private static final int PLAYER_INTERVAL_CLOSE = 20;
	private static final int PLAYER_INTERVAL_FAR = 60 * 5;

	private final transient Map<Peer, Player> playerMap = new WeakHashMap<>();

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

		setContext(new TextInputContext(mainMenu, "Choose a Name:", Settings.getString("multiplayer.name", Resources.generateString("person")), name -> {
			getPlayer().getFaction().setName(name.trim());
			getPlayer().getFaction().setColor(0xFF000000 | Integer.parseInt(Settings.getString("multiplayer.color", String.valueOf(WorldGenerator.randomPlanetColor()))));
			getPlayer().getShip().setName(getPlayer().getFaction().getName());
			getPlayer().getShip().setColor(getPlayer().getFaction().getColor());

			//			Settings.set("multiplayer.name", name);
			//			Settings.set("multiplayer.color", Integer.toHexString(0x00FFFFFF & getPlayer().getFaction().getColor()));

			getPlayer().getShip().getPositionReference().add(PVector.random2D().mult(v.random(200, 1000)));

			connection.joinRoom(DEFAULT_ROOM);
			setContext(this);
			applyContext();

			connection.send(new PlayerJoinMessage(getPlayer()));

			if(!connection.isConnected()) {
				getPlayer().send("Server is not currently available")
						.withColor(DANGER_COLOR);
			}
		}));
		applyContext();
	}

	//// Connection listeners

	@Override
	public void onDisconnect(Peer peer) {
		Player player = playerMap.remove(peer);
		if(player != null) {
			remove(player);
			getPlayer().send(player.getName() + " left the world");
		}
	}

	//// Message listeners

	@Override
	public void onPlayerJoin(Peer peer, PlayerJoinMessage msg) {
		boolean existed = playerMap.containsKey(peer);
		Player player = msg.getPlayer();
		println("<player>", "[" + Long.toHexString(player.getSyncID()) + "]");
		playerMap.put(peer, state.register(player, true));
		if(!existed) {
			getPlayer().send(player.getName() + " joined the world");
			peer.send(new PlayerJoinMessage(getPlayer()));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onSync(Peer peer, SyncMessage msg) {
		long id = msg.getID();
		println("<receive>", msg.getData().getClass().getSimpleName() + "[" + Long.toHexString(id) + "]");
		Syncable object = state.getSyncable(id);
		if(object != null) {
			object.onSync(msg.getData());
		}
		else {
			peer.send(new RequestMessage(id));
		}
	}

	@Override
	public void onRequest(Peer peer, RequestMessage msg) {
		Syncable object = state.getSyncable(msg.getID());
		if(object != null) {
			peer.send(new AddMessage(object));
		}
	}

	@Override
	public void onAdd(Peer peer, AddMessage msg) {
		state.register(msg.getObject(), true);
	}

	@Override
	public void onMove(Peer peer, MoveMessage msg) {
		Syncable object = state.getSyncable(msg.getID());
		if(object instanceof SpaceObject) {
			SpaceObject s = ((SpaceObject)object);
			PVector pos = state.getLocalPosition(msg.getX(), msg.getY());
			PVector vel = state.getLocalVelocity(msg.getVelocity());
			float delay = (System.currentTimeMillis() - msg.getTimestamp()) / 1000F * v.frameRate;
			int interval = getMoveInterval(s);
			s.syncMovement(pos, vel, delay, interval);
		}
	}

	@Override
	public void onRenderLevel(Peer peer, RenderLevelMessage msg) {
		///
	}

	//// World methods

	@Override
	public <T extends Syncable> T register(T object) {
		boolean isNew = state.find(object.getSyncID()) == null;
		object = super.register(object);
		if(isNew && !object.isRemote()) {
//			new Exception().printStackTrace();
			connection.send(new AddMessage(object));
		}
		return object;
	}

	@Override
	public void syncChanges(Syncable object) {
		super.syncChanges(object);
		if(object.shouldPropagate()) {
			println("<broadcast>", object.getClass().getSimpleName() + "[" + Long.toHexString(object.getSyncID()) + "]");
			connection.send(new SyncMessage(object));
		}
	}

	@Override
	public void cleanup() {
		super.cleanup();

		if(connection != null) {
			connection.close();
		}
	}

	private int getMoveInterval(SpaceObject s) {
		if(s instanceof ModularShip && ((ModularShip)s).hasController()) {
			return RenderLevel.SHIP.isVisibleTo(getRenderLevel()) ? PLAYER_INTERVAL_CLOSE : PLAYER_INTERVAL_FAR;
		}
		return 300;
	}

	@Override
	public void render() {
		super.render();

		for(int i = 0; i < state.getObjects().size(); i++) {
			SpaceObject s = state.getObjects().get(i);
			if(!s.isRemote()) {
				// Send periodic movement updates for locally owned objects
				if((v.frameCount + i) % getMoveInterval(s) == 0) {
					connection.send(new MoveMessage(s, state));
				}
			}
		}

		// Periodically print debug info
		if(v.frameCount % 300 == 0) {
			println("----");
			println("Remote Players: " + playerMap.size());
			println("Player Objects: " + state.getSyncables().stream()
					.filter(s -> s instanceof Player)
					.map(s -> ((Player)s).getName())
					.collect(Collectors.joining(", ")));
			println("Ship Objects: " + state.getSyncables().stream()
					.filter(s -> s instanceof Ship)
					.map(s -> ((Ship)s).getName() + ((Ship)s).getPosition())
					.collect(Collectors.joining(", ")));
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
