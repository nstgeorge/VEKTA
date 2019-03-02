package vekta;

import processing.core.PVector;
import processing.sound.LowPass;
import vekta.context.PauseMenuContext;
import vekta.context.World;
import vekta.item.ModuleItem;
import vekta.object.*;
import vekta.object.module.HyperdriveModule;
import vekta.object.module.RCSModule;
import vekta.object.module.TractorBeamModule;
import vekta.overlay.Overlay;
import vekta.overlay.singleplayer.ShipComputerOverlay;
import vekta.overlay.singleplayer.ShipEnergyOverlay;
import vekta.overlay.singleplayer.ShipMoneyOverlay;
import vekta.overlay.singleplayer.TelemetryOverlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static vekta.Vekta.*;

public class Singleplayer implements World {

	private static final float MAX_CAMERA_Y = 5000;

	private static int nextID = 0;

	// Low pass filter
	private LowPass lowPass;

	int planetCount;
	boolean dead;

	PVector cameraPos;
	float cameraSpd;

	PlayerShip playerShip;

	private float zoom = 1; // Camera zoom

	private Counter targetCt = new Counter(30); // Counter for periodically updating Targeter instances
	private Counter spawnCt = new Counter(100); // Counter for periodically cleaning/spawning objects

	UniverseGen generator = new UniverseGen(10000);

	List<SpaceObject> objects = new ArrayList<>();
	List<SpaceObject> markedForDeath = new ArrayList<>();
	List<SpaceObject> markedForAddition = new ArrayList<>();

	List<Overlay> overlays = new ArrayList<>();

	// UI Variables (not real time updated)
	float shortDist;
	float speed;
	String position;

	@Override
	public void init() {
		Vekta v = getInstance();
		v.background(0);

		lowPass = new LowPass(v);

		Resources.setMusic("atmosphere");

		v.frameCount = 0;

		playerShip = new PlayerShip(
				"VEKTA I",
				new PVector(1, 0), // Heading
				new PVector(), // Position
				new PVector(),    // Velocity
				v.color(0, 255, 0),
				0  // Control scheme, Speed, and Handling
		);
		playerShip.getInventory().add(50); // Starting money
		addObject(playerShip);

		Ship ship = new CargoShip(
				"Test Ship",
				new PVector(1, 0), // Heading
				new PVector(500, 500), // Position
				new PVector(),    // Velocity
				v.color(255)
		);
		ship.getInventory().add(new ModuleItem(new RCSModule(2)));
		addObject(ship);

		// TEMP
		playerShip.getInventory().add(new ModuleItem(new HyperdriveModule(1))); // Hyperdrive upgrade for testing
		playerShip.getInventory().add(new ModuleItem(new TractorBeamModule(1))); // Tractor beam upgrade for testing

		// Player ship overlays
		addOverlay(new TelemetryOverlay(playerShip));
		addOverlay(new ShipComputerOverlay(50, -150, playerShip));
		addOverlay(new ShipEnergyOverlay(-300, -100 + 24, playerShip));
		addOverlay(new ShipMoneyOverlay(-300, -100 + 48, playerShip));
	}

	public void addOverlay(Overlay overlay) {
		overlays.add(overlay);
	}

	@Override
	public void render() {
		Vekta v = getInstance();
		v.background(0);

		if(!dead) {
			cameraPos = playerShip.getPosition();
			cameraSpd = playerShip.getVelocity().mag();
			// Camera follow
		}
		else {
			cameraSpd = 0;
		}
		v.camera(cameraPos.x, cameraPos.y, min(MAX_CAMERA_Y, (.07F * cameraSpd + .7F) * (v.height / 2F) / tan(PI * 30 / 180) * zoom), cameraPos.x, cameraPos.y, 0F,
				0F, 1F, 0F);

		cameraPos = playerShip.getPosition();

		boolean targeting = targetCt.cycle();
		boolean spawning = spawnCt.cycle();

		objects.addAll(markedForAddition);
		markedForAddition.clear();

		planetCount = 0;
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
				if(playerShip.getPosition().sub(s.getPosition()).magSq() > generator.getRadius() * generator.getRadius()) {
					removeObject(s);
					continue;
				}
			}

			if(s instanceof Planet) {
				planetCount++;
			}

			s.update();
			s.applyInfluenceVector(objects);
			for(SpaceObject other : objects) {
				if(s != other) {
					// Check both collisions before firing events (prevents race condition)
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
			s.draw();
			s.drawTrail();
		}

		objects.removeAll(markedForDeath);
		markedForDeath.clear();

		if(planetCount < MAX_PLANETS) {
			generator.spawnOccasional(playerShip.getPosition());
		}

		// Info
		if(!dead) {
			// GUI setup
			v.hint(DISABLE_DEPTH_TEST);
			v.camera();
			v.noLights();

			// Set overlay text settings
			v.textFont(bodyFont);
			v.textAlign(LEFT);
			v.textSize(16);
			
			for(Overlay overlay : overlays) {
				overlay.draw();
			}
		}
		// Menus
		else {
			v.hint(DISABLE_DEPTH_TEST);
			v.camera();
			v.noLights();
			v.textFont(headerFont);
			v.textAlign(CENTER, CENTER);

			// Header text
			v.stroke(0);
			v.fill(255, 0, 0);
			v.text("You died.", v.width / 2F, (v.height / 2F) - 100);

			// Body text
			v.stroke(0);
			v.fill(255);
			v.textFont(bodyFont);
			v.text("X TO RETRY", v.width / 2F, (v.height / 2F) + 97);
		}
		v.hint(ENABLE_DEPTH_TEST);
	}

	@Override
	public void keyPressed(char key) {
		if(dead) {
			if(key == 'x') {
				restart();
			}
		}
		else {
			if(key == ESC) {
				setContext(new PauseMenuContext(this));
			}
			if(key == 'k') {
				dead = true;
			}
			if(key == 'r') {
				mouseWheel(-1);
			}
			if(key == 'f') {
				mouseWheel(1);
			}
			playerShip.keyPress(key);
		}
	}

	@Override
	public void keyReleased(char key) {
		playerShip.keyReleased(key);
	}

	@Override
	public void mouseWheel(int amount) {
		zoom = max(.1F, min(3, zoom * (1 + amount * .1F)));
	}

	public void setDead() {
		dead = true;
		if(Resources.getMusic() != null)
			lowPass.process(Resources.getMusic(), 800);
		Resources.stopSound("engine");
		Resources.stopSound("hyperdriveLoop");
		Resources.playSound("death");
	}

	@Override
	public void restart() {
		lowPass.stop();
		Vekta.startWorld(new Singleplayer());
	}

	public PlayerShip getPlayerShip() {
		return playerShip;
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
	public void playSoundAt(String sound, PVector location) {
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
