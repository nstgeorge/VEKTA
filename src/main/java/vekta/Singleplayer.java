package vekta;

import processing.core.PVector;
import processing.sound.LowPass;
import vekta.context.PauseMenuContext;
import vekta.context.World;
import vekta.item.ModuleItem;
import vekta.object.*;
import vekta.object.module.HyperdriveModule;
import vekta.object.module.TargetingModule;
import vekta.object.module.TractorBeamModule;

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

	int health;

	PlayerShip playerShip;

	private float zoom = 1; // Camera zoom

	private Counter targetCt = new Counter(30); // Counter for periodically updating Targeter instances
	private Counter spawnCt = new Counter(100); // Counter for periodically cleaning/spawning objects

	UniverseGen generator = new UniverseGen(10000, 10);

	List<SpaceObject> objects = new ArrayList<>();

	List<SpaceObject> markedForDeath = new ArrayList<>();
	List<SpaceObject> markedForAddition = new ArrayList<>();

	// UI Variables (not real time updated)
	float shortDist;
	float speed;
	float closestMass;
	String position;
	String closestMassUnit;

	@Override
	public void init() {
		Vekta v = getInstance();
		v.background(0);

		lowPass = new LowPass(v);

		Resources.setMusic("atmosphere");

		v.frameCount = 0;

		// Add initial planets
		generator.populate();

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
				v.color(255, 255, 255)
		);
		ship.getInventory().add(new ModuleItem(new HyperdriveModule(1)));
		addObject(ship);

		// TEMP
		playerShip.getInventory().add(new ModuleItem(new HyperdriveModule(1))); // Hyperdrive upgrade for testing
		playerShip.getInventory().add(new ModuleItem(new TractorBeamModule(1))); // Tractor beam upgrade for testing
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
							updateTargeter(s, t);
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
			if(v.frameCount % 10 == 0) {  //  Update values every 10 frames
				updateUIInformation();
			}
			// GUI setup
			v.hint(DISABLE_DEPTH_TEST);
			v.camera();
			v.noLights();

			// Set text stuff
			v.textFont(bodyFont);
			v.textAlign(LEFT);
			v.textSize(16);
			// Draw a rectangle in the bottom
			v.fill(0);
			v.stroke(UI_COLOR);
			v.rectMode(CORNERS);
			v.rect(-1, v.height - 130, v.width + 1, v.height + 1);
			v.fill(UI_COLOR);
			// Text - Far right
			v.text("Health = " + health +
					"\nEnergy = " + (int)playerShip.getEnergy() + " / " + playerShip.getMaxEnergy() + " (" + (int)(playerShip.getEnergy() / playerShip.getMaxEnergy() * 100) + "%)" +
					"\nGold = " + playerShip.getInventory().getMoney(), v.width - 300, v.height - 100);
			// Ship heading indicator
			drawDial("Heading", playerShip.getHeading(), v.width - 370, v.height - 65, 50, v.color(0, 255, 0));
			drawDial("Velocity", playerShip.getVelocity().copy(), v.width - 500, v.height - 65, 50, v.color(0, 255, 0));
			// Text - left
			SpaceObject closestObject = playerShip.getTarget();
			String closestObjectString;
			if(closestObject == null) {
				v.fill(100, 100, 100);
				closestObjectString = "Closest Object: None selected";
			}
			else {
				if(closestObject.getMass() / 1.989e30 < .1) {
					closestMass = (float)round(closestObject.getMass() / 5.9736e24F * 1000) / 1000;
					closestMassUnit = "Earths";
				}
				else {
					closestMass = (float)round(closestObject.getMass() / 1.989e30F * 1000) / 1000;
					closestMassUnit = "Suns";
				}
				if(closestObject instanceof Planet) {
					Planet closestPlanet = (Planet)closestObject;
					closestObjectString = "Closest Object: " + closestObject.getName() + " - " + shortDist + "AU \nHabitable: " + (closestPlanet.isHabitable() ? "YES" : "NO") + "\nMass: " + closestMass + " " + closestMassUnit;
				}
				else {
					closestObjectString = "Closest Object: " + closestObject.getName() + " - " + shortDist + "AU \nSpeed: " + (float)round(closestObject.getVelocity().mag() * 100) / 100 + "\nMass: " + closestMass + " " + closestMassUnit;
				}
				// Closest object arrow
				drawDial("Direction", closestObject.getPosition().sub(cameraPos), 450, v.height - 65, 50, closestObject.getColor());
				v.stroke(closestObject.getColor());
				v.fill(closestObject.getColor());
			}
			v.text(closestObjectString, 50, v.height - 100);
			if(playerShip.isLanding()) {
				//textSize(24);
				v.text(":: Landing Autopilot ::", 50, v.height - 150);
			}
			else if(TargetingModule.isUsingTargeter()) {
				v.text(":: Targeting Computer: planet [1], ship [2] ::", 50, v.height - 150);
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

	private void drawDial(String name, PVector info, int locX, int locY, int radius, int c) {
		Vekta v = getInstance();
		v.fill(0);
		v.stroke(c);
		v.ellipse(locX, locY, radius, radius);
		v.fill(100, 100, 100);
		v.textAlign(CENTER);
		v.textSize(14);
		v.text(name, locX, locY + 25);
		v.textAlign(LEFT);
		v.textSize(16);
		v.stroke(c);
		drawArrow(info, (int)(radius * .8), locX, locY);
		// Reset colors
		v.fill(0);
		v.stroke(UI_COLOR);
	}

	private void drawArrow(PVector heading, int length, int locX, int locY) {
		Vekta v = Vekta.getInstance();
		heading.normalize().mult(length);
		v.line(locX, locY, locX + heading.x, locY + heading.y);
		float angle = heading.heading();
		float x = cos(angle);
		float y = sin(angle);
		PVector endpoint = new PVector(x, y);
		PVector arms = endpoint.copy();
		endpoint.mult(length);
		arms.mult(length * .8F); // scale the arms to a certain length
		// draw the arms
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle - .3F) * (length * .8F), locY + sin(angle - .3F) * (length * .8F));
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle + .3F) * (length * .8F), locY + sin(angle + .3F) * (length * .8F));
	}

	private void updateUIInformation() {
		health = 100;
		shortDist = playerShip.getTarget() != null
				? (float)round(playerShip.getPosition().dist(playerShip.getTarget().getPosition()) * 100) / 100
				: 0;
		speed = (float)round(cameraSpd * 100) / 100;
		position = round(cameraPos.x) + ", " + round(cameraPos.y);
	}

	@Override
	public void keyPressed(char key) {
		if(dead) {
			if(key == 'x') {
				lowPass.stop();
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
	public void updateTargeter(SpaceObject s, Targeter t) {
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
