package vekta;

import vekta.connection.Connection;
import vekta.connection.ConnectionListener;
import vekta.connection.Peer;
import vekta.connection.message.Message;
import vekta.connection.message.PlayerFactionMessage;
import vekta.context.TextInputContext;

import java.util.Map;
import java.util.WeakHashMap;

import static vekta.Vekta.*;

public class Multiplayer extends Singleplayer implements ConnectionListener {
	private static final String SERVER_ADDRESS = "http://vekta-rvanasa.c9users.io"; // TODO: add to settings
	private static final String DEFAULT_ROOM = "Test";

	private final Map<Peer, Faction> playerFactions = new WeakHashMap<>();

	private transient Connection connection;

	@Override
	public void start() {
		super.start();

		connection = new Connection(SERVER_ADDRESS);
		connection.addListener(this);

		setContext(new TextInputContext(mainMenu, "Choose a Name:", Settings.getString("name", Resources.generateString("person")), name -> {
			connection.joinRoom(DEFAULT_ROOM);
			Settings.set("name", name);
			setContext(this);

			broadcast(new PlayerFactionMessage(getPlayer().getFaction()));
		}));
		applyContext();
	}

	@Override
	public void cleanup() {
		super.cleanup();

		connection.close();
	}

	public void broadcast(Message message) {
		try {
			connection.send(message);
		}
		catch(Exception e) {
			//			setContext(mainMenu);
			e.printStackTrace();
			getPlayer().send("Error occurred while sending " + message.getClass().getSimpleName())
					.withColor(DANGER_COLOR);
		}
	}

	@Override
	public void onPlayerFaction(Peer peer, Faction faction) {
		boolean existed = playerFactions.containsKey(peer);
		playerFactions.put(peer, faction);
		if(!existed) {
			getPlayer().send(faction.getName() + " joined the world");
			broadcast(new PlayerFactionMessage(getPlayer().getFaction()));
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
	public void render() {
		super.render();

	}

	@Override
	public void restart() {
		setContext(new Multiplayer());
	}

	@Override
	public void keyPressed(KeyBinding key) {
		if(key == KeyBinding.QUICK_SAVE || key == KeyBinding.QUICK_LOAD) {
			return; // Prevent quick-saving/loading in multiplayer
		}
		super.keyPressed(key);
	}

}
