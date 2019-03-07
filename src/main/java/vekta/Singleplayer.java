package vekta;

import processing.core.PVector;
import processing.sound.LowPass;
import vekta.context.PauseMenuContext;
import vekta.context.World;
import vekta.item.ModuleItem;
import vekta.menu.dialog.Dialog;
import vekta.mission.Mission;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static vekta.Vekta.*;

public class Singleplayer implements World, PlayerListener {
	private static final float ZOOM_EXPONENT = .3F;
	private static final float ZOOM_SMOOTH = .1F;
	private static final float TIME_SCALE = 5e-4F;
	private static final float TIME_FALLOFF = .2F;
	private static final int MAX_OBJECTS_PER_DIST = 5; // TODO: increase as we add more object types

	private static int nextID = 0;

	private int[] objectCounts = new int[RenderLevel.values().length];

	private boolean started;

	// Low pass filter
	private LowPass lowPass;

	// Camera position tracking
	private final PVector cameraPos = new PVector();
	//	private float cameraSpd;

	private Player player;

	private float zoom = 1; // Zoom factor
	private float smoothZoom = zoom; // Smooth zoom factor (converges toward `zoom`)
	private float timeScale = 1; // Camera time scale factor

	private Counter targetCt = new Counter(30); // Counter for periodically updating Targeter instances
	private Counter spawnCt = new Counter(100); // Counter for periodically cleaning/spawning objects

	private final List<SpaceObject> objects = new ArrayList<>();
	private final List<SpaceObject> gravityObjects = new ArrayList<>();
	private final List<SpaceObject> markedForDeath = new ArrayList<>();
	private final List<SpaceObject> markedForAddition = new ArrayList<>();

	private PlayerOverlay overlay;

	public void start() {
		v.frameCount = 0;

		lowPass = new LowPass(v);

		Resources.setMusic("atmosphere");

		WorldGenerator.createSystem(PVector.random2D().mult(1 * AU_DISTANCE));

		player = new Player(UI_COLOR);
		player.addListener(this);

		PlayerShip playerShip = new PlayerShip(
				"VEKTA I",
				PVector.fromAngle(0), // Heading
				new PVector(), // Position
				new PVector(),    // Velocity
				v.color(0, 255, 0)
		);
		playerShip.getInventory().add(50); // Starting money
		addObject(playerShip);
		playerShip.setController(player);

		// Configure UI overlay
		overlay = new PlayerOverlay(player);
		player.addListener(overlay);

		//// Temporary things for testing
		SpaceStation station = new SpaceStation(
				"OUTPOST I",
				new StationCoreModule(),
				new PVector(1, 0),
				// PVector.random2D(), // Heading
				new PVector(100, 100), // Position
				new PVector(),    // Velocity
				//				playerShip.getColor()
				WorldGenerator.randomPlanetColor()
		);
		SpaceStation.Component core = station.getCore();
		SpaceStation.Component rcs = core.attach(SpaceStation.Direction.UP, new RCSModule(1));
		SpaceStation.Component battery = core.attach(SpaceStation.Direction.RIGHT, new BatteryModule(1));
		SpaceStation.Component struct = core.attach(SpaceStation.Direction.LEFT, new StructuralModule(10, 1));
		SpaceStation.Component struct2 = core.attach(SpaceStation.Direction.DOWN, new StructuralModule(10, 1));
		SpaceStation.Component panel = struct.attach(SpaceStation.Direction.LEFT, new SolarArrayModule(1));
		SpaceStation.Component panel2 = struct.attach(SpaceStation.Direction.DOWN, new SolarArrayModule(1));
		SpaceStation.Component panel3 = struct2.attach(SpaceStation.Direction.RIGHT, new SolarArrayModule(1));
		SpaceStation.Component sensor = struct2.attach(SpaceStation.Direction.LEFT, new SensorModule());
		addObject(station);

		playerShip.addModule(new AutopilotModule());
		playerShip.addModule(new TelescopeModule(.5F));
		playerShip.addModule(new DrillModule(2));
		playerShip.getInventory().add(new ModuleItem(new TorpedoModule(2)));
		playerShip.addModule(new HyperdriveModule(.2F));
		playerShip.getInventory().add(new ModuleItem(new TractorBeamModule(1)));
		playerShip.getInventory().add(new ModuleItem(new StructuralModule(3, 1)));

		// Testing out a mission sequence
		Person person = PersonGenerator.randomPerson();
		Mission mission = MissionGenerator.createMission(person);
		Dialog dialog = new Dialog(Resources.generateString("dialog_visit"), person); // TODO: will generate in Player class
		MissionGenerator.createMessenger(player, dialog)
				.getInventory().add(ItemGenerator.randomMissionItem(mission));
	}

	public Player getPlayer() {
		return player;
	}

	public ModularShip getPlayerShip() {
		return player.getShip();
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
	}

	@Override
	public void render() {
		ModularShip playerShip = getPlayerShip();
		if(!playerShip.isDestroyed()) {
			// Camera follow
			cameraPos.set(playerShip.getPosition());
			//			cameraSpd = playerShip.getVelocity().mag();
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

		for(SpaceObject s : markedForAddition) {
			if(!objects.contains(s)) {
				objects.add(s);
			}
			if(s.impartsGravity() && !gravityObjects.contains(s)) {
				gravityObjects.add(s);
			}
		}
		markedForAddition.clear();

		// Reset object counts for each render distance
		for(int i = 0; i < objectCounts.length; i++) {
			objectCounts[i] = 0;
		}

		for(SpaceObject s : objects) {
			if(markedForDeath.contains(s)) {
				continue;
			}

			objectCounts[s.getRenderLevel().ordinal()]++; // Increment count for object's render distance

			// Run on targeting loop
			if(targeting) {
				Collection<Targeter> ts = s.getTargeters();
				if(ts != null) {
					for(Targeter t : ts) {
						if(t.shouldUpdateTarget()) {
							updateTargeters(s);
						}
					}
				}
			}

			if(cleanup) {
				// Clean up distant objects
				float despawnRadius = WorldGenerator.getRadius(s.getRenderLevel());
				if(playerShip.getPosition().sub(s.getPosition()).magSq() >= sq(despawnRadius)) {
					removeObject(s);
					continue;
				}
			}

			s.update(level);
			s.applyGravity(gravityObjects);
			for(SpaceObject other : objects) {
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

		objects.removeAll(markedForDeath);
		gravityObjects.removeAll(markedForDeath);
		markedForDeath.clear();

		if(cleanup && !playerShip.isDestroyed() && !RenderLevel.AROUND_SHIP.isVisibleTo(level)) {
			// Center around zero for improved floating-point precision
			PVector newOrigin = playerShip.getPosition().mult(-1);
			for(SpaceObject s : objects) {
				s.updateOrigin(newOrigin);
			}
		}

		RenderLevel spawnLevel = level;
		while(spawnLevel.ordinal() > 0 && v.random(1) < .05F) {
			spawnLevel = RenderLevel.values()[spawnLevel.ordinal() - 1];
		}
		if(objectCounts[spawnLevel.ordinal()] < MAX_OBJECTS_PER_DIST) {
			WorldGenerator.spawnOccasional(spawnLevel, playerShip);
		}

		v.popMatrix();

		// GUI setup
		//		v.camera();
		//		v.noLights();
		v.hint(DISABLE_DEPTH_TEST);
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
			v.text(Settings.getControlString(ControlKey.MENU_SELECT) + " TO RETRY", v.width / 2F, (v.height / 2F) + 97);
		}
	}

	@Override
	public void keyPressed(ControlKey key) {
		if(getPlayerShip().isDestroyed()) {
			if(key == ControlKey.MENU_SELECT) {
				restart();
			}
		}
		else {
			if(key == ControlKey.MENU_CLOSE) {
				setContext(new PauseMenuContext(this));
			}
			getPlayer().emit(PlayerEvent.KEY_PRESS, key);
		}
	}

	@Override
	public void keyReleased(ControlKey key) {
		getPlayer().emit(PlayerEvent.KEY_RELEASE, key);
	}

	@Override
	public void mouseWheel(int amount) {
		zoom = max(MIN_ZOOM_LEVEL, min(MAX_ZOOM_LEVEL, zoom * (1 + amount * ZOOM_EXPONENT)));
	}

	public void setDead() {
		if(Resources.getMusic() != null)
			lowPass.process(Resources.getMusic(), 800);
		Resources.stopAllSoundsNotMusic();
		Resources.playSound("death");
	}

	@Override
	public void restart() {
		lowPass.stop();
		setContext(new Singleplayer());
	}

	@Override
	public void addObject(Object object) {
		if(object instanceof SpaceObject) {
			SpaceObject s = (SpaceObject)object;
			s.setID(nextID++);
			markedForAddition.add(s);
		}
		else {
			throw new RuntimeException("Cannot add object: " + object);
		}
	}

	@Override
	public void removeObject(Object object) {
		if(object instanceof SpaceObject) {
			markedForDeath.add((SpaceObject)object);
		}
		else {
			throw new RuntimeException("Cannot remove object: " + object);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends SpaceObject> T findRandomObject(Class<T> type) {
		List<T> candidates = new ArrayList<>();
		// TODO: find an efficient way to DRY these loops
		for(SpaceObject s : this.objects) {
			if(type.isInstance(s)) {
				candidates.add((T)s);
			}
		}
		for(SpaceObject s : this.markedForAddition) {
			if(type.isInstance(s)) {
				candidates.add((T)s);
			}
		}
		return candidates.isEmpty() ? null : v.random(candidates);
	}

	@Override
	public SpaceObject findOrbitObject(SpaceObject object) {
		float maxSq = 0;
		SpaceObject bestOrbit = null;
		for(SpaceObject s : gravityObjects) {
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
	public void updateTargeters(SpaceObject s) {
		for(Targeter t : s.getTargeters()) {
			SpaceObject target = t.getTarget();
			float minDistSq = Float.POSITIVE_INFINITY;
			// Search for new targets
			for(SpaceObject other : objects) {
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
}
