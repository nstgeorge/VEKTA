package vekta;

import processing.core.PShape;
import processing.core.PVector;
import processing.sound.LowPass;
import vekta.context.PauseMenuContext;
import vekta.context.World;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.ModuleItem;
import vekta.mission.DockWithObjective;
import vekta.mission.EquipModuleObjective;
import vekta.mission.ItemReward;
import vekta.mission.Mission;
import vekta.module.*;
import vekta.module.station.SensorModule;
import vekta.module.station.SolarArrayModule;
import vekta.module.station.StationCoreModule;
import vekta.module.station.StructuralModule;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.Planet;
import vekta.object.ship.ModularShip;
import vekta.object.ship.PlayerShip;
import vekta.object.ship.SpaceStation;
import vekta.overlay.singleplayer.PlayerOverlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static vekta.Vekta.*;

public class Singleplayer implements World, PlayerListener {
	private static int nextID = 0;

	// Low pass filter
	private LowPass lowPass;

	// Camera position tracking
	private PVector cameraPos;
	private float cameraSpd;

	private Player player;

	private float zoom = 1; // Camera zoom factor

	private Counter targetCt = new Counter(30); // Counter for periodically updating Targeter instances
	private Counter spawnCt = new Counter(100); // Counter for periodically cleaning/spawning objects

	private final List<SpaceObject> objects = new ArrayList<>();
	private final List<SpaceObject> gravityObjects = new ArrayList<>();
	private final List<SpaceObject> markedForDeath = new ArrayList<>();
	private final List<SpaceObject> markedForAddition = new ArrayList<>();

	private PlayerOverlay overlay;

	@Override
	public void start() {
		v.frameCount = 0;

		lowPass = new LowPass(v);

		Resources.setMusic("atmosphere");

		WorldGenerator.createSystem(PVector.random2D().mult(v.random(10000, 15000)));

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
				playerShip.getColor()
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
		playerShip.addModule(new TelescopeModule(2));
		playerShip.addModule(new DrillModule(2));
		playerShip.getInventory().add(new ModuleItem(new TorpedoModule(2)));
		playerShip.getInventory().add(new ModuleItem(new HyperdriveModule(.5F)));
		playerShip.getInventory().add(new ModuleItem(new TractorBeamModule(1)));
		playerShip.getInventory().add(new ModuleItem(new StructuralModule(3, 1)));

		Mission mission2 = new Mission("Test Mission");
		mission2.add(new DockWithObjective(station));
		mission2.start(player);

		Mission mission = new Mission("Two-Option Mission");
		mission.add(new DockWithObjective(station).optional());
		mission.add(new EquipModuleObjective(new HyperdriveModule(1)).optional());
		mission.add(new ItemReward(new Item("Extremely Valuable Thing", ItemType.LEGENDARY)));
		mission.start(player);

		//		player.send("Test notification");///
	}

	public Player getPlayer() {
		return player;
	}

	public ModularShip getPlayerShip() {
		return player.getShip();
	}

	@Override
	public void focus() {
	}

	@Override
	public void render() {
		ModularShip playerShip = getPlayerShip();
		if(!playerShip.isDestroyed()) {
			// Camera follow
			cameraPos = playerShip.getPosition();
			cameraSpd = playerShip.getVelocity().mag();
		}

		v.clear();
		v.rectMode(CENTER);
		v.ellipseMode(RADIUS);

		// Set up world camera
		v.hint(ENABLE_DEPTH_TEST);
		v.camera(cameraPos.x, cameraPos.y, min(MAX_CAMERA_Y, (.07F * cameraSpd + .7F) * (v.height / 2F) / tan(PI * 30 / 180) * zoom), cameraPos.x, cameraPos.y, 0F,
				0F, 1F, 0F);

		boolean targeting = targetCt.cycle();
		boolean spawning = spawnCt.cycle();

		for(SpaceObject obj : markedForAddition) {
			if(obj instanceof Planet && ((Planet)obj).impartsGravity()) {
				gravityObjects.add(obj);
			}
		}
		objects.addAll(markedForAddition);
		markedForAddition.clear();

		for(SpaceObject s : objects) {
			if(markedForDeath.contains(s)) {
				continue;
			}

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

			// Run on spawning loop
			if(spawning) {
				if(playerShip.getPosition().sub(s.getPosition()).magSq() > WorldGenerator.getRadius() * WorldGenerator.getRadius()) {
					removeObject(s);
					continue;
				}
			}

			s.update();
			s.applyInfluenceVector(gravityObjects);
			for(SpaceObject other : objects) {
				if(s != other) {
					// Check both collisions before firing events (prevents race conditions)
					boolean collides1 = s.collidesWith(other);
					boolean collides2 = other.collidesWith(s);
					if(collides1) {
						s.onCollide(other);
					}
					if(collides2) {
						other.onCollide(s);
					}
				}
			}

			// Draw movement trail
			s.drawTrail();

			// Draw object
			v.pushMatrix();
			PVector position = s.getPositionReference();
			v.translate(position.x, position.y);
			v.stroke(s.getColor());
			v.fill(0);
			s.draw();
			v.popMatrix();
		}

		objects.removeAll(markedForDeath);
		gravityObjects.removeAll(markedForDeath);
		markedForDeath.clear();

		if(gravityObjects.size() < MAX_PLANETS) {
			WorldGenerator.spawnOccasional(playerShip.getPosition());
		}

		// GUI setup
		v.camera();
		v.noLights();
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
			v.text("X TO RETRY", v.width / 2F, (v.height / 2F) + 97);
		}
	}

	@Override
	public void keyPressed(char key) {
		if(getPlayerShip().isDestroyed()) {
			if(key == 'x') {
				restart();
			}
		}
		else {
			if(key == ESC) {
				setContext(new PauseMenuContext(this));
			}
			if(key == 'k') {
				getPlayerShip().destroyBecause(getPlayerShip());
			}
			getPlayerShip().onKeyPress(key);
		}
	}

	@Override
	public void keyReleased(char key) {
		getPlayerShip().onKeyRelease(key);
	}

	@Override
	public void mouseWheel(int amount) {
		zoom = max(.1F, min(3, zoom * (1 + amount * .1F)));
	}

	public void setDead() {
		if(Resources.getMusic() != null)
			lowPass.process(Resources.getMusic(), 800);
		Resources.stopAllSounds();
		Resources.playSound("death");
	}

	@Override
	public void restart() {
		lowPass.stop();
		startWorld(new Singleplayer());
	}

	@Override
	public void addObject(Object object) {
		if(object instanceof SpaceObject) {
			SpaceObject s = (SpaceObject)object;
			s.setID(nextID++);
			markedForAddition.add(s);
			if(object instanceof PlayerListener) {
				getPlayer().addListener((PlayerListener)object);
			}
		}
		else {
			throw new RuntimeException("Cannot addFeature object: " + object);
		}
	}

	@Override
	public void removeObject(Object object) {
		if(object instanceof SpaceObject) {
			markedForDeath.add((SpaceObject)object);
			if(object instanceof PlayerListener) {
				getPlayer().removeListener((PlayerListener)object);
			}
		}
		else {
			throw new RuntimeException("Cannot remove object: " + object);
		}
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

	/**
	 * Utility for drawing secondary details that will only appear after a certain zoom level.
	 *
	 * @param shape Shape to draw
	 */
	public void drawSecondary(PShape shape) {
		if(zoom < 2) {
			shape.stroke(150, UI_COLOR);
			shape.fill(0);
			v.shape(shape);
		}
	}

	public void drawSecondaryLine(float x1, float y1, float x2, float y2) {
		if(zoom < 2) {
			v.stroke(0, 150, 0);
			v.line(x1, y1, x2, y2);
			v.stroke(UI_COLOR);
		}
	}
}
