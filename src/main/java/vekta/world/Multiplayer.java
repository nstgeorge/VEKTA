package vekta.world;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import processing.core.PVector;
import vekta.Resources;
import vekta.Settings;
import vekta.connection.Connection;
import vekta.connection.ConnectionListener;
import vekta.connection.Peer;
import vekta.connection.message.*;
import vekta.context.TextInputContext;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.BackButton;
import vekta.menu.option.CustomButton;
import vekta.menu.option.MissionButton;
import vekta.menu.option.PlayerButton;
import vekta.mission.Mission;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.player.Player;
import vekta.spawner.WorldGenerator;
import vekta.sync.Syncable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static vekta.Vekta.*;

public class Multiplayer extends Singleplayer implements ConnectionListener {
	private static final String SERVER_ADDRESS =
			Settings.getString("multiplayer.server", "http://vekta-rvanasa.c9users.io");
	private static final String DEFAULT_ROOM =
			Settings.getString("multiplayer.room", "Default");

	private static final int PLAYER_INTERVAL_CLOSE = 10;
	private static final int PLAYER_INTERVAL_FAR = 60;

	private transient Connection connection;
	private boolean unableToConnect = false;

	private final transient BiMap<Peer, Player> playerMap = HashBiMap.create();
	private final transient Map<Peer, Float> timeMap = new WeakHashMap<>();
	private final transient Set<Syncable> changesToSend = new HashSet<>();

	private float timeScale = 1;

	@Override
	public float getTimeScale() {
		return timeScale;
	}

	@Override
	public void start() {
		connection = new Connection(SERVER_ADDRESS);
		connection.addListener(this);

		super.start();

		setContext(new TextInputContext(mainMenu, "Choose a Name:", Settings.getString("multiplayer.name", Resources.generateString("person")), name -> {
			getPlayer().getFaction().setName(name);
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
		}));
		//		applyContext();
	}

	//// Connection listeners

	@Override
	public void onConnect() {
		unableToConnect = false;
	}

	@Override
	public void onConnectError() {
		if(!unableToConnect) {
			unableToConnect = true;
			getPlayer().send("Server is not currently available")
					.withColor(DANGER_COLOR);
		}
	}

	@Override
	public void onDisconnect(Peer peer) {
		Player player = playerMap.remove(peer);
		if(player != null) {
			remove(player);
			remove(player.getShip());
			getPlayer().send(player.getName() + " left the world");
		}
		if(timeMap.remove(peer) != null) {
			recomputeTimeScale();
		}
	}

	//// Message listeners

	@Override
	public void onPlayerJoin(Peer peer, PlayerJoinMessage msg) {
		boolean existed = playerMap.containsKey(peer);
		Player player = msg.getPlayer();
		println("<player>", "[" + Long.toHexString(player.getSyncID()) + "]");
		playerMap.put(peer, player);
		if(!existed) {
			getPlayer().send(player.getName() + " joined the world");
			peer.send(new PlayerJoinMessage(getPlayer()));
		}
		peer.send(new TimeScaleMessage(getTimeScale()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onSyncObject(Peer peer, SyncMessage msg) {
		if(msg.getData() instanceof Player) {
			println("Warning: received Player in a SyncMessage");
			return;
		}

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
	public void onRequestObject(Peer peer, RequestMessage msg) {
		Syncable object = state.getSyncable(msg.getID());
		if(object != null) {
			peer.send(new CreateMessage(object, state.getGlobalOffset()));
		}
	}

	@Override
	public void onCreateObject(Peer peer, CreateMessage msg) {
		//		Syncable object = state.register(msg.getObject(), true);
		Syncable object = msg.getObject();
		if(object instanceof SpaceObject) {
			SpaceObject s = (SpaceObject)object;
			s.getPositionReference().add(state.getGlobalOffset().relativePosition(msg.getOffset()));
			s.addVelocity(state.getGlobalOffset().relativeVelocity(msg.getOffset()));
		}
	}

	@Override
	public void onMoveObject(Peer peer, MoveMessage msg) {
		Syncable object = state.getSyncable(msg.getID());
		if(object instanceof SpaceObject) {
			SpaceObject s = ((SpaceObject)object);
			PVector pos = state.getLocalPosition(msg.getX(), msg.getY());
			PVector vel = state.getLocalVelocity(msg.getVelocity());
			float delay = (System.currentTimeMillis() - msg.getTimestamp()) / 1000F * v.frameRate;
			int interval = getMoveInterval(s);
			s.syncMovement(pos, vel, delay, interval);
		}
		else if(object == null) {
			peer.send(new RequestMessage(msg.getID()));
		}
	}

	@Override
	public void onChangeRenderLevel(Peer peer, TimeScaleMessage msg) {
		float scale = msg.getTimeScale();
		timeMap.put(peer, scale);
		recomputeTimeScale();
	}

	@Override
	public void onShareMission(Peer peer, ShareMissionMessage msg) {
		Player sender = playerMap.get(peer);
		Mission mission = msg.getMission();

		println(mission.getObjectives(), mission.getStatus());//////

		//		for(Objective objective : mission.getObjectives()) {
		//			objective.onStart(mission);
		//		}

		Menu menu = new Menu(getPlayer(), new BackButton(getContext()), new SpaceObjectMenuHandle(sender.getShip()));
		menu.add(new MissionButton(mission));
		menu.addDefault();
		setContext(menu);
	}

	//// World methods

	@Override
	public void populateWorld() {
		// Override singleplayer world population
	}

	@Override
	public <T extends Syncable> T register(T object) {
		boolean isNew = state.find(object.getSyncID()) == null;
		object = super.register(object);
		if(isNew && !object.isRemote()) {
			connection.send(new CreateMessage(object, state.getGlobalOffset()));
		}
		return object;
	}

	@Override
	public void sendChanges(Syncable object) {
		super.sendChanges(object);

		if(object.shouldSendChanges()) {
			changesToSend.add(object);
		}
	}

	@Override
	public void sendMessage(Player player, Message message) {
		Peer peer = playerMap.inverse().get(player);
		if(peer != null) {
			peer.send(message);
		}
		else {
			super.sendMessage(player, message);
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

	// Notify remote clients of new time level
	private void broadcastTimeScale() {
		connection.send(new TimeScaleMessage(super.getTimeScale()));
		recomputeTimeScale();
	}

	// Compute time level based on other player's states
	private void recomputeTimeScale() {
		float scale = super.getTimeScale();
		for(float other : timeMap.values()) {
			if(other < scale) {
				scale = other;
			}
		}
		timeScale = scale;
	}

	@Override
	public void focus() {
		super.focus();
		broadcastTimeScale();
	}

	@Override
	public void unfocus() {
		super.unfocus();
		//		if(connection != null) {// TEMP conditional
		connection.send(new TimeScaleMessage(Float.POSITIVE_INFINITY));
		//		}
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

		for(Syncable object : changesToSend) {
			println("<broadcast>", object.getClass().getSimpleName() + "[" + Long.toHexString(object.getSyncID()) + "]");
			connection.send(new SyncMessage(object));
		}
		changesToSend.clear();

		// Periodically print debug info
		if(v.frameCount % 300 == 0) {
			println("----");
			println("Time Scale: " + getTimeScale());
			println("Remote Players: " + playerMap.size());
			println("Player Objects: " + state.getSyncables().stream()
					.filter(s -> s instanceof Player)
					.map(s -> ((Player)s).getName())
					.collect(Collectors.joining(", ")));
			//			println("SpaceObjects: " + state.getSyncables().stream()
			//					.filter(s -> s instanceof SpaceObject)
			//					.map(s -> ((SpaceObject)s).getName())
			//					.collect(Collectors.joining(", ")));
		}
	}

	@Override
	public void onMenu(Menu menu) {
		super.onMenu(menu);

		////
		if(menu.getHandle() instanceof SpaceObjectMenuHandle && ((SpaceObjectMenuHandle)menu.getHandle()).getSpaceObject() != getPlayer().getShip()) {
			println("!!!!! " + getPlayer().getShip().getName() + " " + ((SpaceObjectMenuHandle)menu.getHandle()).getSpaceObject());///
		}

		if(menu.getHandle() instanceof SpaceObjectMenuHandle && ((SpaceObjectMenuHandle)menu.getHandle()).getSpaceObject() == getPlayer().getShip()) {
			println(playerMap);////
			if(!playerMap.isEmpty()) {
				menu.add(new CustomButton("Players", m -> {
					Menu sub = new Menu(m.getPlayer(), new BackButton(m), new MenuHandle());
					for(Player player : playerMap.values()) {
						sub.add(new PlayerButton(player));
					}
					setContext(sub);
				}));
			}
		}
	}

	@Override
	protected void onZoomChange(float zoom) {
		super.onZoomChange(zoom);
		broadcastTimeScale();
	}

	@Override
	public void restart() {
		cleanup();

		setContext(new Multiplayer());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Syncable> List<T> findObjects(Class<T> type) {
		if(Player.class.equals(type)) {
			return Lists.asList((T)getPlayer(), (T[])playerMap.values().toArray());
		}
		return super.findObjects(type);
	}

	@Override
	public boolean load(File file) {
		return false;
	}

	@Override
	public boolean save(File file) {
		return false;
	}
}
