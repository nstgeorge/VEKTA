package vekta;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.sound.LowPass;
import vekta.connection.message.Message;
import vekta.context.PauseMenuContext;
import vekta.context.World;
import vekta.item.ColonyItem;
import vekta.item.ModuleItem;
import vekta.menu.Menu;
import vekta.menu.handle.MainMenuHandle;
import vekta.module.*;
import vekta.module.station.SensorModule;
import vekta.module.station.SolarArrayModule;
import vekta.module.station.StationCoreModule;
import vekta.module.station.StructuralModule;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.ship.ModularShip;
import vekta.object.ship.PlayerShip;
import vekta.object.ship.SpaceStation;
import vekta.overlay.singleplayer.PlayerOverlay;
import vekta.person.Person;
import vekta.sound.SoundGroup;
import vekta.spawner.EconomyGenerator;
import vekta.spawner.EventGenerator;
import vekta.spawner.MissionGenerator;
import vekta.spawner.WorldGenerator;
import vekta.spawner.world.StarSystemSpawner;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static vekta.Vekta.*;

public class Singleplayer implements World, PlayerListener {
	private static final File QUICKSAVE_FILE = new File("quicksave.vekta");
	private static final File AUTOSAVE_FILE = new File("autosave.vekta");

	private static final float ZOOM_EXPONENT = .3F;
	private static final float ZOOM_SMOOTH = .1F;
	//	private static final float TIME_SCALE = 5e-4F;
	//	private static final float TIME_FALLOFF = .2F;
	private static final float TIME_SCALE = .001F;
	private static final float TIME_FALLOFF = .1F;
	private static final int MAX_OBJECTS_PER_DIST = 5; // TODO: increase as we add more object types

	private static final float MAX_AUDITORY_DISTANCE = 3000; // Used for calculating volume of sounds. Higher = hear more
	private static final float MAX_PAN_DISTANCE = 1000; // Distance where sound is panned entirely left/right

	private static final float MIN_PLANET_TIME_SCALE = 10; // Make traveling between ship-level objects much faster

	private static final SoundGroup MUSIC = new SoundGroup("atmosphere");

	private int[] objectCounts = new int[RenderLevel.values().length];

	private boolean started;

	// Low pass filter
	private final transient LowPass lowPass = new LowPass(v);

	protected WorldState state;

	private float zoom = 1; // Zoom factor
	private float smoothZoom = zoom; // Smooth zoom factor (converges toward `zoom`)
	private float timeScale = 1; // Camera time scale factor
	private RenderLevel prevLevel = RenderLevel.PARTICLE;

	private final Counter targetCt = new Counter(30).randomize(); // Update Targeter instances
	//	private final Counter spawnCt = new Counter(10).randomize(); // Spawn objects
	private final Counter cleanupCt = new Counter(100).randomize(); // Despawn objects
	private final Counter eventCt = new Counter(3600 * 5).randomize(); // Occasional random events
	private final Counter situationCt = new Counter(100).randomize(); // Situational events
	private final Counter economyCt = new Counter(600).randomize(); // Economic progression

	private PlayerOverlay overlay;

	public Singleplayer() {
	}

	public Singleplayer(WorldState state) {
		this.state = state;
	}

	public void start() {
		v.frameCount = 0;
		Resources.stopMusic();

		if(state == null) {
			if(load(AUTOSAVE_FILE)) {
				return;
			}
			else {
				// Run initial setup if no autosave
				setup();
			}
		}

		Player player = getPlayer();

		// Configure UI overlay
		overlay = new PlayerOverlay(player);
		player.removeListeners(PlayerOverlay.class);
		player.addListener(overlay);
	}

	public void setup() {
		state = new WorldState();

		PlayerFaction playerFaction = register(new PlayerFaction("VEKTA I", UI_COLOR));
		Player player = register(new Player(playerFaction));
		player.addListener(this);
		state.setPlayer(player);

		PlayerShip playerShip = register(new PlayerShip(
				player.getFaction().getName(),
				PVector.fromAngle(0), // Heading
				new PVector(), // Position
				new PVector(), // Velocity
				v.color(0, 255, 0)
		));
		playerShip.setPersistent(true);
		playerShip.getInventory().add(50); // Starting money
		playerShip.setController(player);

		populateWorld();

		setupTesting(); // Temporary
	}

	public void cleanup() {
		getPlayer().removeListener(this);

		// Cleanup behavior on exiting/restarting the world
		lowPass.stop();
	}

	public void populateWorld() {
		StarSystemSpawner.createSystem(PVector.random2D().mult(2 * AU_DISTANCE));
	}

	private void setupTesting() {
		ModularShip playerShip = getPlayerShip();

		if(getClass() == Singleplayer.class) {
			// Add station to singleplayer world
			SpaceStation station = register(new SpaceStation(
					"OUTPOST I",
					new StationCoreModule(),
					new PVector(1, 0),
					new PVector(300, 100), // Position
					new PVector(),    // Velocity
					playerShip.getColor()
			));
			SpaceStation.Component core = station.getCore();
			SpaceStation.Component rcs = core.attach(SpaceStation.Direction.UP, new RCSModule(1));
			SpaceStation.Component orbiter = core.attach(SpaceStation.Direction.RIGHT, new OrbitModule(1));
			SpaceStation.Component struct = core.attach(SpaceStation.Direction.LEFT, new StructuralModule(10, 1));
			SpaceStation.Component struct2 = core.attach(SpaceStation.Direction.DOWN, new StructuralModule(10, 1));
			SpaceStation.Component panel = struct.attach(SpaceStation.Direction.LEFT, new SolarArrayModule(1));
			SpaceStation.Component panel2 = struct.attach(SpaceStation.Direction.DOWN, new SolarArrayModule(1));
			SpaceStation.Component panel3 = struct2.attach(SpaceStation.Direction.RIGHT, new SolarArrayModule(1));
			SpaceStation.Component sensor = struct2.attach(SpaceStation.Direction.LEFT, new SensorModule());
		}

		//		playerShip.getInventory().add(new DialogItemSpawner().create());////

		playerShip.addModule(new EngineModule(2)); // Upgrade engine
		playerShip.addModule(new AutopilotModule());
		playerShip.addModule(new AntennaModule());
		playerShip.addModule(new TelescopeModule(.5F));
		playerShip.addModule(new DrillModule(2));
		playerShip.addModule(new HyperdriveModule());
		playerShip.addModule(new ActiveTCSModule(2));
		playerShip.addModule(new CountermeasureModule());
		playerShip.getInventory().add(new ModuleItem(new PlanetBusterModule()));
		playerShip.getInventory().add(new ModuleItem(new GeneratorModule()));
		playerShip.getInventory().add(new ModuleItem(new WormholeModule()));
		playerShip.getInventory().add(new ModuleItem(new TorpedoModule(2)));
		playerShip.getInventory().add(new ModuleItem(new TractorBeamModule(1)));
		playerShip.getInventory().add(new ModuleItem(new StructuralModule(3, 1)));
		playerShip.getInventory().add(new ModuleItem(new StationCoreModule(1)));
		playerShip.getInventory().add(new ModuleItem(new OrbitModule(1)));
		playerShip.getInventory().add(new ColonyItem());
	}

	public Player getPlayer() {
		return state.getPlayer();
	}

	public ModularShip getPlayerShip() {
		return getPlayer().getShip();
	}

	@Override
	public RenderLevel getRenderLevel() {
		return getRenderDistance(getZoom());
	}

	@Override
	public float getTimeScale() {
		return timeScale;
	}

	public float getZoom() {
		return smoothZoom;
	}

	@Override
	public void focus() {
		if(!started) {
			started = true;
			start();
		}
		else {
			save(AUTOSAVE_FILE);
		}
	}

	@Override
	public void render() {
		ModularShip playerShip = getPlayerShip();

		// Cycle background music
		if(Resources.getMusic() == null) {
			Resources.setMusic(MUSIC.random(), false);
		}

		float prevTimeScale = timeScale;

		// Update time factor
		smoothZoom += (zoom - smoothZoom) * ZOOM_SMOOTH;
		timeScale = max(1, smoothZoom * TIME_SCALE) / (1 + smoothZoom * TIME_SCALE * TIME_SCALE * TIME_FALLOFF);

		// Determine render level from time scale
		RenderLevel level = getRenderLevel();

		// Speed up ship-to-ship movement
		if(level == RenderLevel.PLANET && timeScale < MIN_PLANET_TIME_SCALE) {
			timeScale = MIN_PLANET_TIME_SCALE;
		}

		// Counteract velocity mismatch on player zoom
		if(prevTimeScale != timeScale && prevLevel == level) {
			playerShip.getPositionReference()
					.add(playerShip.getVelocity().mult(timeScale - prevTimeScale));
		}

		v.clear();
		v.rectMode(CENTER);
		v.ellipseMode(RADIUS);

		v.pushMatrix();
		v.translate(v.width / 2F, v.height / 2F);

		boolean targeting = targetCt.cycle();
		//		boolean spawning = spawnCt.cycle();
		boolean cleanup = cleanupCt.cycle();

		updateGlobal(level);

		state.startUpdate();

		// Reset object counts for each render distance
		for(int i = 0; i < objectCounts.length; i++) {
			objectCounts[i] = 0;
		}

		// Update loop
		for(SpaceObject s : state.getObjects()) {
			if(state.isRemoving(s)) {
				continue;
			}

			if(!s.isPersistent()) {
				// Increment count for object's render level
				objectCounts[s.getDespawnLevel().ordinal()]++;

				if(cleanup) {
					// Clean up distant objects
					float despawnRadius = WorldGenerator.getRadius(s.getDespawnLevel());
					if(playerShip.getPosition().sub(s.getPosition()).magSq() >= sq(despawnRadius)) {
						s.despawn();
						continue;
					}
				}
			}

			if(targeting) {
				// Update Targeter instances
				s.updateTargets();
			}

			s.update(level);
			s.applyGravity(state.getGravityObjects());
			for(SpaceObject other : state.getObjects()) {
				if(s != other) {
					// Check both collisions before firing events (prevents race conditions)
					// TODO: colliders should check each other rather than themselves
					boolean collides1 = s.collidesWith(level, other);
					boolean collides2 = other.collidesWith(level, s);
					if(collides1) {
						s.onCollide(other);
					}
					if(collides2) {
						other.onCollide(s);
					}
				}
			}
		}

		state.endUpdate();

		// Draw loop
		for(SpaceObject s : state.getObjects()) {
			// Start drawing object
			v.pushMatrix();

			// Set up object position
			PVector position = s.getPositionReference();
			PVector cameraPos = playerShip.getPositionReference();
			float scale = getZoom();
			float screenX = (position.x - cameraPos.x) / scale;
			float screenY = (position.y - cameraPos.y) / scale;
			v.translate(screenX, screenY);

			// Draw trail
			s.updateTrail();
			if(s == playerShip || s.getRenderLevel().isVisibleTo(level)) {
				s.drawTrail(scale);
			}

			// Draw object
			v.stroke(s.getColor());
			v.noFill();
			float r = s.getRadius() / scale;
			float onScreenRadius = s.getOnScreenRadius(r);
			if(abs(screenX) - onScreenRadius <= v.width / 2 && abs(screenY) - onScreenRadius <= v.height / 2) {
				s.draw(level, r);
			}

			// End drawing object
			v.popMatrix();
		}

		RenderLevel spawnLevel = level;
		while(spawnLevel.ordinal() > 0 && v.chance(.05F)) {
			spawnLevel = RenderLevel.values()[spawnLevel.ordinal() - 1];
		}
		if(objectCounts[spawnLevel.ordinal()] < MAX_OBJECTS_PER_DIST) {
			WorldGenerator.spawnOccasional(spawnLevel, playerShip);
		}

		if(eventCt.cycle()) {
			eventCt.randomize();
			EventGenerator.spawnEvent(getPlayer());
		}

		if(situationCt.cycle()) {
			EventGenerator.updateSituations(getPlayer());
		}

		if(economyCt.cycle()) {
			List<Faction> factions = state.getFactions();
			if(!factions.isEmpty()) {
				EconomyGenerator.updateFaction(v.random(factions));
				for(Faction faction : factions) {
					faction.getEconomy().update();
				}
			}
			getPlayer().getFaction().getEconomy().update();
		}

		prevLevel = level;
		v.popMatrix();

		// GUI setup
		if(!playerShip.isDestroyed()) {
			overlay.render();
		}
		else {
			v.textFont(headerFont);
			v.textAlign(CENTER, CENTER);

			// Header text
			v.stroke(0);
			v.fill(255, 0, 0);
			v.text("You died.", v.width / 2F, v.height / 2F - 100);

			// Body text
			v.stroke(0);
			v.fill(255);
			v.textFont(bodyFont);
			v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to load autosave", v.width / 2F, (v.height / 2F) + 97);
		}
	}

	protected void updateGlobal(RenderLevel level) {
		ModularShip playerShip = getPlayerShip();

		// Set global velocity relative to player ship when zoomed in
		if(RenderLevel.SHIP.isVisibleTo(level)) {
			state.addRelativeVelocity(playerShip.getVelocity());
		}
		else if(level.ordinal() == prevLevel.ordinal() + 1) {
			state.resetRelativeVelocity();
			state.addRelativeVelocity(findLargestObject().getVelocity());
		}
		state.updateGlobalCoords(getTimeScale());

		if(level.ordinal() == prevLevel.ordinal() - 1 && !playerShip.isDestroyed()) {
			// Center around zero for improved floating-point precision
			state.addRelativePosition(playerShip.getPosition());
		}
	}

	protected void onZoomChange(float zoom) {
		// Overridden by Multiplayer
	}

	// Temp: debug key listener
	@Override
	public void keyPressed(KeyEvent event) {
		if(v.key == '`') {
			MissionGenerator.createMission(getPlayer(), MissionGenerator.randomMissionPerson(), (int)v.random(5) + 1).start();
		}
		//		if(v.key == '`') {
		//			getPlayer().getInventory().add(ClothingItemSpawner.createDisguiseItem(FactionGenerator.randomFaction()));
		//		}
		World.super.keyPressed(event);
	}

	@Override
	public void keyPressed(KeyBinding key) {
		if(key == KeyBinding.QUICK_LOAD) {
			load(QUICKSAVE_FILE);
		}
		else if(getPlayerShip().isDestroyed()) {
			if(key == KeyBinding.MENU_SELECT) {
				reload();
			}
		}
		else {
			if(key == KeyBinding.QUICK_SAVE && save(QUICKSAVE_FILE)) {
				getPlayer().send("Progress saved");
			}
			if(key == KeyBinding.MENU_CLOSE) {
				setContext(new PauseMenuContext(this));
			}
			getPlayer().emit(PlayerEvent.KEY_PRESS, key);
		}
	}

	@Override
	public void keyReleased(KeyBinding key) {
		getPlayer().emit(PlayerEvent.KEY_RELEASE, key);
	}

	@Override
	public void mouseWheel(int amount) {
		zoom = max(MIN_ZOOM_LEVEL, min(MAX_ZOOM_LEVEL, zoom * (1 + amount * ZOOM_EXPONENT)));
		onZoomChange(zoom);
	}

	@Override
	public <T extends Syncable> T register(T object) {
		return state.register(object);
	}

	@Override
	public void remove(Syncable object) {
		state.remove(object);
	}

	@Override
	public void sendChanges(Syncable object) {
		// Do nothing in singleplayer
	}

	@Override
	public void sendMessage(Player player, Message message) {
		println("Attempted to send " + message.getClass().getSimpleName() + " to unavailable player: " + player.getName());
	}

	// TODO: convert to player event callback
	@Override
	public void setDead() {
		// TODO: custom death soundtrack instead of low pass filter?
		if(Resources.getMusic() != null) {
			lowPass.process(Resources.getMusic(), 800);
		}
		Resources.stopAllSoundsExceptMusic();
		Resources.playSound("death");
	}

	@Override
	public void reload() {
		if(!load(AUTOSAVE_FILE)) {
			restart();
		}
	}

	@Override
	public void restart() {
		cleanup();

		Singleplayer world = new Singleplayer(new WorldState());
		world.setup();
		setContext(world);
		applyContext();
	}

	public SpaceObject findLargestObject() {
		float maxMass = 0;
		SpaceObject max = null;
		for(SpaceObject s : state.getObjects()) {
			float mass = s.getMass();
			if(mass > maxMass) {
				maxMass = mass;
				max = s;
			}
		}
		return max;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Syncable> List<T> findObjects(Class<T> type) {
		List candidates;
		if(SpaceObject.class.isAssignableFrom(type)) {
			candidates = state.getObjects();
		}
		else if(Person.class.isAssignableFrom(type)) {
			candidates = state.getPeople();
		}
		else if(Faction.class.isAssignableFrom(type)) {
			candidates = state.getFactions();
		}
		else if(Player.class.isAssignableFrom(type)) {
			candidates = Collections.singletonList(getPlayer());
		}
		else {
			throw new RuntimeException("Unrecognized object type: " + type.getName());
		}

		List<T> list = new ArrayList<>();
		for(Object obj : candidates) {
			if(type.isInstance(obj)) {
				list.add((T)obj);
			}
		}
		return list;
	}

	@Override
	public <T extends Syncable> T findRandomObject(Class<T> type) {
		List<T> candidates = findObjects(type);
		return candidates.isEmpty() ? null : v.random(candidates);
	}

	@Override
	public SpaceObject findOrbitObject(SpaceObject object) {
		float maxSq = 0;
		SpaceObject bestOrbit = null;
		for(SpaceObject s : state.getGravityObjects()) {
			if(s != object) {
				float weightSq = object.getGravityAcceleration(Collections.singletonList(s)).magSq() / s.getMass();
				if(weightSq > maxSq) {
					maxSq = weightSq;
					bestOrbit = s;
				}
			}
		}
		return bestOrbit;
	}

	@Override
	public void updateTargeter(Targeter t) {
		if(t.shouldUpdateTarget()) {
			SpaceObject s = t.getSpaceObject();
			SpaceObject target = t.getTarget();
			float minDistSq = Float.POSITIVE_INFINITY;
			// Search for new targets
			for(SpaceObject other : state.getObjects()) {
				if(s != other && t.isValidTarget(other)) {
					float distSq = s.getPosition().sub(other.getPosition()).magSq();
					if(distSq < minDistSq) {
						minDistSq = distSq;
						target = other;
					}
				}
			}
			t.setTarget(target);
		}
	}

	@Override
	public void playSound(String sound, PVector location) {
		float distance = getPlayerShip().getPosition().dist(location);
		float distanceX = getPlayerShip().getPosition().x - location.x;

		// Pan
		float pan = (MAX_PAN_DISTANCE - distanceX) / MAX_PAN_DISTANCE;
		if(pan < -1)
			pan = -1;
		if(pan > 1)
			pan = 1;

		// Volume
		float maxDistance = MAX_AUDITORY_DISTANCE * getZoom();
		float volume = (maxDistance - distance) / maxDistance;
		if(volume < 0)
			volume = 0;
		if(volume > 1)
			volume = 1;

		Resources.playSound(sound);
		Resources.setSoundVolume(sound, volume);
		Resources.setSoundPan(sound, pan);
		//		Resources.resetSoundVolumeAndPan(sound);
	}

	public boolean load(File file) {
		if(!file.exists()) {
			return false;
		}

		try {
			Singleplayer world = new Singleplayer(Format.read(new FileInputStream(file)));
			setContext(world);
			applyContext();

			println("Loaded from " + file);
			return true;
		}
		catch(InvalidClassException | ClassNotFoundException e) {
			println("Outdated file format: " + file);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean save(File file) {
		try {
			Format.write(state, new FileOutputStream(file));
			println("Saved to " + file);
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			getPlayer().send("Failed to save progress: " + e.getMessage())
					.withColor(v.color(255, 0, 0));
			return false;
		}
	}

	// PlayerListener callbacks

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof MainMenuHandle) {
			cleanup();
		}
	}
}
