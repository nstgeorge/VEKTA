package vekta.world;

import processing.core.PVector;
import processing.event.KeyEvent;
import vekta.Format;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;
import vekta.overlay.singleplayer.DebugOverlay;
import vekta.profiler.Profiler;
import vekta.connection.message.Message;
import vekta.context.KnowledgeContext;
import vekta.context.PauseMenuContext;
import vekta.context.Starfield;
import vekta.context.TextInputContext;
import vekta.dungeon.Dungeon;
import vekta.economy.Economy;
import vekta.ecosystem.Ecosystem;
import vekta.faction.Faction;
import vekta.faction.PlayerFaction;
import vekta.item.ColonyItem;
import vekta.item.ModuleItem;
import vekta.knowledge.*;
import vekta.menu.Menu;
import vekta.menu.handle.DebugMenuHandle;
import vekta.menu.handle.MainMenuHandle;
import vekta.menu.option.BackButton;
import vekta.menu.option.CustomButton;
import vekta.menu.option.DungeonRoomButton;
import vekta.module.*;
import vekta.module.station.StationCoreModule;
import vekta.module.station.StructuralModule;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.ModularShip;
import vekta.object.ship.PlayerShip;
import vekta.object.ship.SpaceStation;
import vekta.overlay.singleplayer.PlayerOverlay;
import vekta.person.Person;
import vekta.player.Player;
import vekta.player.PlayerEvent;
import vekta.player.PlayerListener;
import vekta.sound.SoundGroup;
import vekta.spawner.*;
import vekta.spawner.item.BlueprintItemSpawner;
import vekta.spawner.item.ClothingItemSpawner;
import vekta.spawner.item.WeaponItemSpawner;
import vekta.spawner.world.BlackHoleSpawner;
import vekta.spawner.world.SpaceStationSpawner;
import vekta.spawner.world.StarSystemSpawner;
import vekta.sync.Syncable;
import vekta.terrain.settlement.Settlement;
import vekta.util.Counter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static vekta.Vekta.*;

public class Singleplayer implements World, PlayerListener {
	private static final File QUICKSAVE_FILE = new File("quicksave.vekta");
	private static final File AUTOSAVE_FILE = new File("autosave.vekta");

	private static Starfield starfield;

	private static final float ZOOM_FACTOR = .3F;
	private static final float ZOOM_SMOOTH = .1F;
	private static final float TIME_SCALE = .001F;
	private static final float TIME_FALLOFF = .1F;
	private static final int MAX_OBJECTS_PER_DIST = 5; // TODO: increase as we add more object types

	private static final float MAX_AUDITORY_DISTANCE = 2000; // Used for calculating volume of sounds. Higher = hear more
	private static final float MAX_PAN_DISTANCE = 3000; // Distance where sound is panned entirely left/right

	private static final float MIN_PLANET_TIME_SCALE = 10; // Make traveling between ship-level objects much faster

	private static final SoundGroup MUSIC = new SoundGroup("atmosphere");

	private final int[] objectCounts = new int[RenderLevel.values().length];

	private boolean started;

	// Low pass filter
	//    private final transient LowPass lowPass = new LowPass(v);

	protected WorldState state;

	private boolean lastZoomOutward; // Was last user-controlled zoom directed outward?
	private float smoothZoom = 10; // Time-smoothed zoom factor
	private float timeScale = 1; // World time scale
	private RenderLevel prevLevel = RenderLevel.PARTICLE;

	private final Counter targetCt = new Counter(30).randomize(); // Update Targeter instances
	private final Counter spawnCt = new Counter(10).randomize(); // Spawn objects
	private final Counter cleanupCt = new Counter(100).randomize(); // Despawn objects
	private final Counter eventCt = new Counter(3600 * 20).randomize(); // Occasional random events
	private final Counter situationCt = new Counter(30).randomize(); // Situational events
	private final Counter economyCt = new Counter(600).randomize(); // Economic progression
	private final Counter ecosystemCt = new Counter(600).randomize(); // Ecosystem progression

	private final Profiler profiler = new Profiler();
	private PlayerOverlay overlay;
	private DebugOverlay debugOverlay;

	private float cameraImpact;

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
		else if(state.getPlayer() == null) {
			setup();////
		}

		Player player = getPlayer();

		// Configure UI overlay
		overlay = new PlayerOverlay(player);
		player.removeListeners(PlayerOverlay.class);
		player.addListener(overlay);

		starfield = new Starfield(this);
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
		playerShip.observe(ObservationLevel.OWNED, getPlayer());
		playerShip.getInventory().add(50); // Starting money
		playerShip.setController(player);

		populateWorld();
	}

	public void cleanup() {
		// Cleanup behavior on exiting/restarting the world
		getPlayer().removeListener(this);
		//        lowPass.stop();
	}

	public void populateWorld() {
		StarSystemSpawner.createSystem(PVector.random2D().mult(2 * AU_DISTANCE));

		setContext(new TextInputContext(mainMenu, "Choose a Name:", Settings.getString("singleplayer.name", Resources.generateString("person")), name -> {
			getPlayer().getFaction().setName(name);
			getPlayer().getFaction().setColor(0xFF000000 | Integer.parseInt(Settings.getString("singleplayer.color", String.valueOf(UI_COLOR))));
			getPlayer().getShip().setColor(getPlayer().getFaction().getColor());

			setContext(this);
			applyContext();
		}));
	}

	private void setupTesting() {
		Player player = getPlayer();
		ModularShip playerShip = player.getShip();

		if(getClass() == Singleplayer.class) {
			// Add station to singleplayer world
			SpaceStation station = SpaceStationSpawner.createStation("OUTPOST 1", PVector.random2D().mult(1000), getPlayer().getColor());
			station.observe(ObservationLevel.OWNED, getPlayer());

			BlackHoleSpawner.createBlackHole(PVector.random2D().mult(v.random(40, 50) * AU_DISTANCE))
					.observe(ObservationLevel.OWNED, getPlayer());
		}

		for(int i = 0; i < 3; i++) {
			PersonGenerator.createPerson().setFaction(getPlayer().getFaction());
		}

		player.addKnowledge(new StoryKnowledge(StoryGenerator.createStory(), "Story"));

		playerShip.addModule(new EngineModule(2)); // Upgrade engine
		playerShip.addModule(new AutopilotModule());
		playerShip.addModule(new AntennaModule());
		playerShip.addModule(new TelescopeModule(.5F));
		playerShip.addModule(new DrillModule(2));
		playerShip.addModule(new HyperdriveModule());
		playerShip.addModule(new ActiveTCSModule(2));
		playerShip.addModule(new CountermeasureModule());
		playerShip.addModule(new ShieldModule());
		playerShip.getInventory().add(new ModuleItem(new OceanScannerModule()));
		playerShip.getInventory().add(new ModuleItem(new EcosystemScannerModule()));
		playerShip.getInventory().add(new ModuleItem(new PlanetBusterModule()));
		playerShip.getInventory().add(new ModuleItem(new GeneratorModule()));
		playerShip.getInventory().add(new ModuleItem(new WormholeModule()));
		playerShip.getInventory().add(new ModuleItem(new TorpedoModule(2)));
		playerShip.getInventory().add(new ModuleItem(new FractalGunModule(2)));
		playerShip.getInventory().add(new ModuleItem(new TractorBeamModule(1)));
		playerShip.getInventory().add(new ModuleItem(new StructuralModule(3, 1)));
		playerShip.getInventory().add(new ModuleItem(new StationCoreModule(1)));
		playerShip.getInventory().add(new ModuleItem(new OrbitModule(1)));
		playerShip.getInventory().add(new ColonyItem());
		playerShip.getInventory().add(BlueprintItemSpawner.randomBlueprint());
		playerShip.getInventory().add(WeaponItemSpawner.randomWeapon());
	}

	public Player getPlayer() {
		return state.getPlayer();
	}

	@Override
	public RenderLevel getRenderLevel() {
		return getRenderDistance(getZoom());
	}

	@Override
	public float getTime() {
		return state.getTime();
	}

	@Override
	public float getTimeScale() {
		return timeScale;
	}

	@Override
	public float getZoom() {
		return smoothZoom;
	}

	@Override
	public void setZoom(float zoom) {
		if(!Float.isFinite(zoom)) {
			zoom = 1;
		}
		float prevZoom = state.getZoom();
		for(ZoomController controller : state.getZoomControllers()) {
			zoom = controller.controlZoom(getPlayer(), zoom);
		}
		if(zoom != prevZoom) {
			onZoomChange(zoom);
		}
		state.setZoom(max(MIN_ZOOM_LEVEL, min(MAX_ZOOM_LEVEL, zoom)));
		if(zoom > prevZoom) {
			lastZoomOutward = true;
		}
		else if(zoom < prevZoom) {
			lastZoomOutward = false;
		}
	}

	@Override
	public void setAutoZoom(float zoom) {
		// Only zoom if player was zooming in the same direction
		if(lastZoomOutward ? zoom > state.getZoom() : zoom < state.getZoom()) {
			setZoom(zoom);
		}
	}

	@Override
	public void setAutoZoomDirection(boolean outward) {
		lastZoomOutward = outward;
	}

	@Override
	public void addZoomController(ZoomController controller) {
		state.addZoomController(controller);
		setZoom(state.getZoom());
	}

	@Override
	public void addCameraImpact(float amount) {
		cameraImpact += amount;
	}

	@Override
	public void schedule(float delay, Callback callback) {
		state.schedule(delay, callback);
	}

	@Override
	public void focus() {
		if(!started) {
			started = true;
			start();
		}
		else {
			autosave();
		}
	}

	@Override
	public void render() {
		// Take timestamp: render loop begin
		profiler.reset();
		profiler.addTimeStamp("Frame Start");

		Player player = getPlayer();
		ModularShip playerShip = player.getShip();

		// Cycle background music
		if(Resources.getMusic() == null && Settings.getInt("music") > 0) {
			Resources.setMusic(MUSIC.random(), false);
		}

		// Update time factor
		smoothZoom += (state.getZoom() - smoothZoom) * ZOOM_SMOOTH;
		timeScale = max(1, smoothZoom * TIME_SCALE) / (1 + smoothZoom * TIME_SCALE * TIME_SCALE * TIME_FALLOFF);

		// Determine render level from time scale
		RenderLevel level = getRenderLevel();

		// Speed up ship-to-ship movement
		if(level == RenderLevel.PLANET && timeScale < MIN_PLANET_TIME_SCALE) {
			timeScale = MIN_PLANET_TIME_SCALE;
		}

		// Take timestamp: Setup
		profiler.addTimeStamp("Setup and Zoom");

		v.clear();
		v.rectMode(CENTER);
		v.ellipseMode(RADIUS);

		v.pushMatrix();
		v.translate(v.width / 2F, v.height / 2F);

		float zoom = state.getZoom();
		float zoomRatio = zoom / smoothZoom;
		if(zoomRatio > 1) {
			zoomRatio = 1 / zoomRatio;
		}
		float zoomStrength = 1 - zoomRatio;
		if(zoomStrength > 1e-5F) {
			v.noFill();
			float minZoom = 1 / smoothZoom;
			float maxZoom = MAX_ZOOM_LEVEL / smoothZoom;
			for(float r = minZoom; r < maxZoom; r *= 10) {
				if(r >= 1) {
					break;
				}
				if(r >= 1e-3F) {
					v.stroke(v.lerpColor(0, 50, zoomStrength * r));
					v.rect(0, 0, r * v.width, r * v.height);
				}
			}
		}

		profiler.addTimeStamp("Zoom box drawing");

		List<ZoomController> zoomControllers = state.getZoomControllers();
		for(int i = zoomControllers.size() - 1; i >= 0; i--) {
			if(zoomControllers.get(i).shouldCancelZoomControl(player)) {
				zoomControllers.remove(i);
			}
		}

		if(cameraImpact > 1e-3F) {
			v.fill(v.lerpColor(0, 255, min(1, cameraImpact)));
			v.rect(0, 0, v.width + 2, v.height + 2);
			cameraImpact *= .95F;
		}

		boolean targeting = targetCt.cycle();
		boolean cleanup = cleanupCt.cycle();

		updateGlobal(level);

		profiler.addTimeStamp("Begin update");

		state.startUpdate();

		// Reset object counts for each render distance
		Arrays.fill(objectCounts, 0);

		// Pre-update loop
		List<SpaceObject> objects = state.getObjects();
		for(SpaceObject s : objects) {
			if(!s.isPersistent()) {
				// Increment count for object's render level
				objectCounts[s.getDespawnLevel().ordinal()]++;

				// Occasionally clean up distant objects
				if(cleanup) {
					float despawnRadius = WorldGenerator.getRadius(s.getDespawnLevel());
					if(playerShip.getPosition().sub(s.getPosition()).magSq() >= sq(despawnRadius)) {
						s.despawn();
						continue;
					}
				}
			}

			// Occasionally update targets
			if(targeting) {
				s.updateTargets();
			}

			// Move to next position
			s.applyVelocity(s.getVelocityReference());
		}

		profiler.addTimeStamp("Object cleanup and target updates");

		v.pushMatrix();
		starfield.draw(playerShip);
		v.popMatrix();

		profiler.addTimeStamp("Starfield drawing");

		// Custom behavior loop
		// If the debug overlay is enabled, separate the loop in several independent ones to determine performance
		// This is obviously way worse for performance, but I don't think there's any way around it right now
		// TODO: Make this neater
		if(debugOverlay != null) {
			// Gravity loop
			for(int i = 0, size = objects.size(); i < size; i++) {
				// Skip despawned/destroyed objects
				if(state.isRemoving(objects.get(i))) {
					continue;
				}
				updateGravity(objects, level, i);
			}
			profiler.addTimeStamp("Apply gravity");

			// Drawing loop
			for(int i = 0, size = objects.size(); i < size; i++) {
				// Skip despawned/destroyed objects
				if(state.isRemoving(objects.get(i))) {
					continue;
				}
				drawObjects(objects, level, i, playerShip);
			}
			profiler.addTimeStamp("Draw objects");

			// Collision loop
			for(int i = 0, size = objects.size(); i < size; i++) {
				// Skip despawned/destroyed objects
				if(state.isRemoving(objects.get(i))) {
					continue;
				}
				resolveCollisions(objects, level, i, playerShip);
			}
			profiler.addTimeStamp("Resolve collisions");
		}
		else {
			for(int i = 0, size = objects.size(); i < size; i++) {
				SpaceObject s = objects.get(i);
				// Skip despawned/destroyed objects
				if(state.isRemoving(s)) {
					continue;
				}

				updateGravity(objects, level, i);
				drawObjects(objects, level, i, playerShip);
				resolveCollisions(objects, level, i, playerShip);
			}
		}

		state.endUpdate();

		if(spawnCt.cycle()) {
			RenderLevel spawnLevel = level;
			while(spawnLevel.ordinal() > 0 && v.chance(.05F)) {
				spawnLevel = RenderLevel.values()[spawnLevel.ordinal() - 1];
			}
			if(objectCounts[spawnLevel.ordinal()] < MAX_OBJECTS_PER_DIST) {
				WorldGenerator.spawnOccasional(spawnLevel, playerShip);
			}
		}

		if(eventCt.cycle() && Settings.getBoolean("randomEvents", true)) {
			eventCt.randomize();
			EventGenerator.spawnEvent(player);
		}

		if(situationCt.cycle()) {
			EventGenerator.updateSituations(player);
		}

		if(economyCt.cycle()) {
			List<Economy> economiesToRemove = new ArrayList<>(1);
			for(Economy economy : state.getEconomies()) {
				if(economy.isAlive()) {
					economy.update();
				}
				else {
					economiesToRemove.add(economy);
				}
			}
			state.getEconomies().removeAll(economiesToRemove);
		}

		if(ecosystemCt.cycle()) {
			for(SpaceObject s : state.getObjects()) {
				if(s instanceof TerrestrialPlanet) {
					Ecosystem ecosystem = ((TerrestrialPlanet)s).getTerrain().getEcosystem();
					ecosystem.update();
				}
			}
		}

		profiler.addTimeStamp("Spawns, event, economy, and ecosystem");

		prevLevel = level;
		v.popMatrix();

		// GUI setup
		if(!playerShip.isDestroyed()) {
			overlay.render();
			if(debugOverlay != null) {
				debugOverlay.render();
			}
		}
		else {
			v.textFont(HEADER_FONT);
			v.textAlign(CENTER, CENTER);

			// Header text
			v.stroke(0);
			v.fill(255, 0, 0);
			v.text("You died.", v.width / 2F, v.height / 2F - 100);

			// Body text
			v.stroke(0);
			v.fill(255);
			v.textFont(BODY_FONT);
			v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to load autosave", v.width / 2F, (v.height / 2F) + 97);
		}

		profiler.addTimeStamp("Overlay");

		//		if(cameraImpact > 1) {
		//			v.fill(v.lerpColor(255, 100, 1 / cameraImpact));
		//			v.rectMode(CORNERS);
		//			v.rect(0, 0, v.width, v.height);
		//			v.rectMode(CENTER);
		//		}
	}

	private void updateGravity(List<SpaceObject> objects, RenderLevel level, int i) {
		SpaceObject s = objects.get(i);

		// Move towards gravitational objects
		s.applyGravity(state.getGravityObjects());

		// Update object
		s.update(level);
	}

	private void drawObjects(List<SpaceObject> objects, RenderLevel level, int i, ModularShip playerShip) {
		SpaceObject s = objects.get(i);
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
		if((s == playerShip || s.getRenderLevel().isVisibleTo(level)) && Settings.getBoolean("drawTrails")) {
			s.updateTrail();
			s.drawTrail(scale);
		}

		// Draw object
		v.stroke(s.getColor());
		v.noFill();
		float r = s.getRadius() / scale;
		float onScreenRadius = s.getOnScreenRadius(r);
		boolean visible = abs(screenX) - onScreenRadius <= v.width / 2F && abs(screenY) - onScreenRadius <= v.height / 2F;
		if(visible) {
			s.draw(level, r);
		}

		// End drawing object
		v.popMatrix();
	}

	private void resolveCollisions(List<SpaceObject> objects, RenderLevel level, int i, ModularShip playerShip) {
		SpaceObject s = objects.get(i);
		float scale = getZoom();
		PVector position = s.getPositionReference();
		PVector cameraPos = playerShip.getPositionReference();
		float screenX = (position.x - cameraPos.x) / scale;
		float screenY = (position.y - cameraPos.y) / scale;
		float r = s.getRadius() / scale;
		float onScreenRadius = s.getOnScreenRadius(r);
		boolean visible = abs(screenX) - onScreenRadius <= v.width / 2F && abs(screenY) - onScreenRadius <= v.height / 2F;
		// Check collisions when on screen
		if(visible) {
			for(int j = i + 1; j < objects.size(); j++) {
				SpaceObject other = objects.get(j);

				// Ensure collision is reasonable
				if(s.getRenderLevel().isVisibleTo(level) || other.getRenderLevel().isVisibleTo(level)) {
					// Check both collision conditions before interacting
					if(s.collidesWith(level, other) && other.collidesWith(level, s)) {
						s.onCollide(other);
						other.onCollide(s);
					}
				}
			}
		}
	}

	protected void updateGlobal(RenderLevel level) {
		ModularShip playerShip = getPlayer().getShip();

		// Set global velocity relative to player ship when zoomed in
		if(RenderLevel.SHIP.isVisibleTo(level)/* || Resources.getSound("hyperdriveLoop").isPlaying()*/) {
			state.addRelativeVelocity(playerShip.getVelocity());
		}
		else if(level.ordinal() == prevLevel.ordinal() + 1) {
			state.resetRelativeVelocity();
			state.addRelativeVelocity(findLargestObject().getVelocity());
		}
		state.updateGlobalCoords(getTimeScale());

		//		if(level.ordinal() == prevLevel.ordinal() - 1 && !playerShip.isDestroyed()) {
		//			// Center around zero for improved floating-point precision
		//			state.addRelativePosition(playerShip.getPosition());
		//		}
		if(!playerShip.isDestroyed()) {
			// Center around zero for improved floating-point precision
			//			state.addRelativePosition(playerShip.getPosition().mult(.1F));
			state.addRelativePosition(playerShip.getPosition());
		}
	}

	protected void onZoomChange(float zoom) {
		// Overridden by Multiplayer
	}

	@Override
	public boolean globalKeyPressed(KeyEvent event) {
		if(event.getKeyCode() == Settings.getKeyCode(KeyBinding.SHIP_KNOWLEDGE) && !(getContext() instanceof KnowledgeContext)) {
			setContext(new KnowledgeContext(getContext(), getPlayer()));
			return true;
		}
		return false;
	}

	@Override
	public void keyPressed(KeyBinding key) {
		if(key == KeyBinding.ZOOM_IN) {
			setZoom(getZoom() / 10);
		}
		else if(key == KeyBinding.ZOOM_OUT) {
			setZoom(getZoom() * 10);
		}
		else if(key == KeyBinding.QUICK_LOAD) {
			load(QUICKSAVE_FILE);
		}
		else if(getPlayer().getShip().isDestroyed()) {
			if(key == KeyBinding.MENU_SELECT) {
				reload();
			}
		}
		else {
			if(key == KeyBinding.QUICK_SAVE && save(QUICKSAVE_FILE)) {
				getPlayer().send("Progress saved");
			}
			else if(key == KeyBinding.MENU_CLOSE) {
				setContext(new PauseMenuContext(getContext()));
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
		setZoom(state.getZoom() * (1 + amount * ZOOM_FACTOR * Settings.getFloat("zoomSpeed")));
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

	@Override
	public void reload() {
		if(!load(AUTOSAVE_FILE)) {
			restart();
		}
	}

	@Override
	public void restart() {
		cleanup();
		setContext(new Singleplayer(new WorldState()));
	}

	public SpaceObject findLargestObject() {
		float maxMass = 0;
		SpaceObject max = null;
		List<SpaceObject> objects = state.getGravityObjects();
		if(objects.isEmpty()) {
			objects = state.getObjects();
		}
		for(SpaceObject s : objects) {
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
		else if(Economy.class.isAssignableFrom(type)) {
			candidates = state.getEconomies();
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
		ModularShip playerShip = getPlayer().getShip();

		float distance = playerShip.getPosition().dist(location);
		float distanceX = playerShip.getPosition().x - location.x;

		// Pan
		float pan = (MAX_PAN_DISTANCE - distanceX) / MAX_PAN_DISTANCE;
		pan = pan > 1 ? 1 : pan < -1 ? -1 : pan; // Clamp between -1 and 1

		// Volume
		float maxDistance = MAX_AUDITORY_DISTANCE * getZoom();
		float volume = (maxDistance - distance) / maxDistance;
		volume = volume > 1 ? 1 : volume < 0 ? 0 : volume; // Clamp between 0 and 1

		Resources.playSound(sound, volume, pan);
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

	@Override
	public void autosave() {
		save(AUTOSAVE_FILE);
	}

	// PlayerListener callbacks

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof MainMenuHandle) {
			cleanup();
		}
	}

	@Override
	public void onGameOver(ModularShip ship) {
		// TODO: custom death soundtrack instead of low pass filter?
		//        if (Resources.getMusic() != null) {
		//            lowPass.process(Resources.getMusic(), 800);
		//        }
		Resources.stopAllSoundsExceptMusic();
		Resources.playSound("death");
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKey() == '`' && Settings.getBoolean("debug")) {
			if(!getPlayer().hasAttribute(DebugAttribute.class)) {
				getPlayer().addAttribute(DebugAttribute.class);
				setupTesting();
				getPlayer().send("Debug mode enabled");
			}
			Menu menu = new Menu(getPlayer(), new BackButton(this), new DebugMenuHandle());
			menu.add(new CustomButton("Toggle Overlay", m -> {
				if(debugOverlay == null) {
					debugOverlay = new DebugOverlay(profiler);
				}
				else {
					debugOverlay = null;
				}
				m.close();
			}));
			menu.add(new CustomButton("Give Missions", m -> {
				for(int i = 0; i < 10; i++) {
					MissionGenerator.createMission(getPlayer(), MissionGenerator.randomMissionPerson(), (int)v.random(5) + 1).start();
				}
				m.close();
			}));
			menu.add(new CustomButton("Give Knowledge", m -> {
				for(TerrestrialPlanet planet : findObjects(TerrestrialPlanet.class)) {
					getPlayer().addKnowledge(new TerrestrialKnowledge(ObservationLevel.VISITED, planet));
					for(Settlement settlement : planet.getTerrain().getSettlements()) {
						getPlayer().addKnowledge(new SettlementKnowledge(ObservationLevel.VISITED, settlement));
					}
				}
				for(Person person : findObjects(Person.class)) {
					getPlayer().addKnowledge(new PersonKnowledge(ObservationLevel.VISITED, person));
				}
				setContext(new KnowledgeContext(this, getPlayer()));
			}));
			menu.add(new CustomButton("Enemy Factions & Give Disguise", m -> {
				for(Faction faction : state.getFactions()) {
					faction.setEnemy(getPlayer().getFaction());
					getPlayer().getInventory().add(ClothingItemSpawner.createDisguiseItem(faction));
				}
				m.close();
			}));
			menu.add(new CustomButton("Enter Dungeon", m -> {
				Dungeon dungeon = DungeonGenerator.createDungeon(PersonGenerator.randomHome());
				new DungeonRoomButton(dungeon.getName(), dungeon.getStartRoom()).onSelect(m);
			}));
			menu.addDefault();
			setContext(menu);
		}
		World.super.keyPressed(event);
	}

	// Player tag for debug mode

	private static class DebugAttribute implements Player.Attribute {
	}
}
