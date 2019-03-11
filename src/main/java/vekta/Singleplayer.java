package vekta;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.sound.LowPass;
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
import vekta.sound.Tune;
import vekta.spawner.EventGenerator;
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

	private static final SoundGroup MUSIC = new SoundGroup("atmosphere");

	private int[] objectCounts = new int[RenderLevel.values().length];

	private boolean started;

	// Low pass filter
	private final transient LowPass lowPass = new LowPass(v);

	// Camera position tracking
	private final PVector cameraPos = new PVector();

	protected final WorldState state;
	private final boolean loaded;

	private float zoom = 1; // Zoom factor
	private float smoothZoom = zoom; // Smooth zoom factor (converges toward `zoom`)
	private float timeScale = 1; // Camera time scale factor
	private RenderLevel prevLevel = RenderLevel.PARTICLE;

	// TODO: move to WorldState
	private final Counter targetCt = new Counter(30); // Counter for periodically updating Targeter instances
	private final Counter spawnCt = new Counter(100); // Counter for periodically cleaning/spawning objects
	private final Counter eventCt = new Counter(3600 * 5).randomize(); // Counter for random player events

	private PlayerOverlay overlay;

	private Tune __tune;

	public Singleplayer() {
		Faction playerFaction = new Faction("VEKTA I", UI_COLOR);
		Player player = new Player(playerFaction);

		this.state = new WorldState(player);
		loaded = false;
	}

	public Singleplayer(WorldState state) {
		this.state = state;
		loaded = true;
	}

	public void start() {
		v.frameCount = 0;
		Resources.stopMusic();

		Player player = getPlayer();
		player.addListener(this);

		if(!loaded) {
			StarSystemSpawner.createSystem(PVector.random2D().mult(2 * AU_DISTANCE));

			PlayerShip playerShip = register(new PlayerShip(
					player.getFaction().getName(),
					PVector.fromAngle(0), // Heading
					new PVector(), // Position
					new PVector(),    // Velocity
					v.color(0, 255, 0)
			));
			playerShip.getInventory().add(50); // Starting money
			playerShip.setController(player);

			setupTesting(); // Temporary
		}

		// Configure UI overlay
		overlay = new PlayerOverlay(player);
		player.addListener(overlay);
	}

	public void cleanup() {
		// Cleanup behavior on exiting/restarting the world
		lowPass.stop();
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

		playerShip.addModule(new EngineModule(2)); // Upgrade engine
		playerShip.addModule(new AutopilotModule());
		playerShip.addModule(new TelescopeModule(.5F));
		playerShip.addModule(new DrillModule(2));
		playerShip.addModule(new HyperdriveModule(.5F));
		playerShip.getInventory().add(new ModuleItem(new TorpedoModule(2)));
		playerShip.getInventory().add(new ModuleItem(new TractorBeamModule(1)));
		playerShip.getInventory().add(new ModuleItem(new StructuralModule(3, 1)));
		playerShip.getInventory().add(new ModuleItem(new StationCoreModule(1)));
		playerShip.getInventory().add(new ModuleItem(new OrbitModule(1)));
		playerShip.getInventory().add(new ColonyItem());

		//		// Testing out a mission sequence
		//		Person person = PersonGenerator.createPerson();
		//		MissionGenerator.createMessenger(player, MissionGenerator.randomApproachDialog(player, person));
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

		save(AUTOSAVE_FILE);

		// Uncomment for testing
		// __tune = TuneGenerator.randomTune();
	}

	@Override
	public void render() {
		ModularShip playerShip = getPlayerShip();
		if(!playerShip.isDestroyed()) {
			// Camera follow
			cameraPos.set(playerShip.getPosition());
			//			cameraSpd = playerShip.getVelocity().mag();
		}

		// Cycle background music
		if(Resources.getMusic() == null) {
			Resources.setMusic(MUSIC.random(), false);
		}

		if(__tune != null) {
			__tune.update();////temp
		}

		// Update time factor
		smoothZoom += (zoom - smoothZoom) * ZOOM_SMOOTH;
		timeScale = max(1, smoothZoom * TIME_SCALE) / (1 + smoothZoom * TIME_SCALE * TIME_SCALE * TIME_FALLOFF);

		v.clear();
		v.rectMode(CENTER);
		v.ellipseMode(RADIUS);

		// Set up world camera
		//		v.hint(ENABLE_DEPTH_TEST);
		//		v.camera(cameraPos.x, cameraPos.y, min(MAX_CAMERA_Y, (.07F * cameraSpd + .7F) * (v.height / 2F) / tan(PI * 30 / 180) * timeScale), cameraPos.x, cameraPos.y, 0F,
		//				0F, 1F, 0F);

		//		println(gravityObjects.size(), timeScale);

		RenderLevel level = getRenderLevel();

		v.pushMatrix();
		v.translate(v.width / 2F, v.height / 2F);

		boolean targeting = targetCt.cycle();
		boolean cleanup = spawnCt.cycle();

		state.startUpdate();

		// Reset object counts for each render distance
		for(int i = 0; i < objectCounts.length; i++) {
			objectCounts[i] = 0;
		}

		for(SpaceObject s : state.getObjects()) {
			if(state.isRemoving(s)) {
				continue;
			}

			objectCounts[s.getRenderLevel().ordinal()]++; // Increment count for object's render distance

			// Run on targeting loop
			if(targeting) {
				s.updateTargets();
			}

			if(cleanup) {
				// Clean up distant objects
				float despawnRadius = WorldGenerator.getRadius(s.getRenderLevel());
				if(playerShip.getPosition().sub(s.getPosition()).magSq() >= sq(despawnRadius)) {
					remove(s);
					continue;
				}
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

			if(s == playerShip) {
				// Fix camera position when zoomed in at high velocity
				cameraPos.set(s.getPositionReference());
			}

			// Draw object
			v.pushMatrix();
			PVector position = s.getPositionReference();
			float scale = getZoom();
			float screenX = (position.x - cameraPos.x) / scale;
			float screenY = (position.y - cameraPos.y) / scale;
			v.translate(screenX, screenY);
			s.updateTrail();
			if(s == playerShip || s.getRenderLevel().isVisibleTo(level)) {
				s.drawTrail(scale);
			}
			v.stroke(s.getColor());
			v.noFill();
			float r = s.getRadius() / scale;
			float onScreenRadius = s.getOnScreenRadius(r);
			if(abs(screenX) - onScreenRadius <= v.width / 2 && abs(screenY) - onScreenRadius <= v.height / 2) {
				s.draw(level, r);
			}
			v.popMatrix();
		}

		state.endUpdate();

//		if(level.ordinal() == prevLevel.ordinal() - 1 && !playerShip.isDestroyed()) {
//			// Center around zero for improved floating-point precision
//			state.addRelativePosition(playerShip.getPosition());
//		}
//
//		// Change global relative velocity to player ship when zoomed in
//		if(RenderLevel.SHIP.isVisibleTo(level)) {
//			state.addRelativeVelocity(playerShip.getVelocity());
//		}
//		else if(level.ordinal() == prevLevel.ordinal() + 1) {
//			state.resetRelativeVelocity();
//		}

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

		prevLevel = level;
		v.popMatrix();

		// GUI setup
		//		v.camera();
		//		v.noLights();
		//		v.hint(DISABLE_DEPTH_TEST);
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

	// Temp: debug key listener
	@Override
	public void keyPressed(KeyEvent event) {
		if(v.key == '`') {
			println("====");
			for(Syncable s : state.getSyncables()) {
				print(s.getSyncKey());
			}
			println("====");
		}
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
	public void apply(Syncable object) {
		// Do nothing in singleplayer
	}

	// TODO: convert to player event callback
	@Override
	public void setDead() {
		// TODO: custom death soundtrack instead of low pass filter?
		if(Resources.getMusic() != null) {
			lowPass.process(Resources.getMusic(), 800);
		}
		Resources.stopAllSoundsNotMusic();
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

		Singleplayer world = new Singleplayer();
		setContext(world);
		switchContext();
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
	public <T> T findRandomObject(Class<T> type) {
		List<T> candidates = new ArrayList<>();
		// TODO: find an efficient way to DRY these loops
		if(SpaceObject.class.isAssignableFrom(type)) {
			for(SpaceObject s : state.getObjects()) {
				if(type.isInstance(s)) {
					candidates.add((T)s);
				}
			}
		}
		else if(Person.class.isAssignableFrom(type)) {
			for(Person p : state.getPeople()) {
				if(type.isInstance(p)) {
					candidates.add((T)p);
				}
			}
		}
		else if(Faction.class.isAssignableFrom(type)) {
			for(Faction f : state.getFactions()) {
				if(type.isInstance(f)) {
					candidates.add((T)f);
				}
			}
		}
		else {
			throw new RuntimeException("Unrecognized object type: " + type.getName());
		}
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
		float volume = (MAX_AUDITORY_DISTANCE - distance) / MAX_AUDITORY_DISTANCE;
		if(volume < 0)
			volume = 0;
		if(volume > 1)
			volume = 1;

		Resources.setSoundVolume(sound, volume);
		Resources.setSoundPan(sound, pan);
		Resources.playSound(sound);
		Resources.resetSoundVolumeAndPan(sound);
	}

	public boolean load(File file) {
		if(!file.exists()) {
			return false;
		}

		try {
			setContext(new Singleplayer(Format.read(new FileInputStream(file))));
			switchContext();
			println("Loaded from " + file);
			return true;
		}
		catch(InvalidClassException e) {
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
					.withColor(DANGER_COLOR);
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
