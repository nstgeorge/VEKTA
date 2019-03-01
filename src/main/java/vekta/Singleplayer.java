package vekta;

import processing.core.PVector;
import processing.sound.LowPass;
import vekta.context.PauseMenuContext;
import vekta.context.World;
import vekta.object.Planet;
import vekta.object.SpaceObject;
import vekta.object.Spaceship;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.*;

public class Singleplayer implements World {
	private static int nextID = 0;

	// Low pass filter
	private LowPass lowPass;

	int planetCount;
	boolean dead;
	PVector pos;

	PVector lastKnownPosition;

	float spd;
	int health;
	int ammunition = 100;

	float minDistSq = Float.POSITIVE_INFINITY;

	Spaceship playerShip;
	public SpaceObject closestObject; // TODO: move this logic into Spaceship instances

	float zoom = 1; // Camera zoom

	UniverseGen generator = new UniverseGen(20000, 10);

	List<SpaceObject> objects = new ArrayList<SpaceObject>();

	List<SpaceObject> markedForDeath = new ArrayList<SpaceObject>();
	List<SpaceObject> markedForAddition = new ArrayList<SpaceObject>();

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
		
		lowPass = lowPass = new LowPass(v);

//		if(getSetting("music") > 0 && !atmosphere.isPlaying())
//			atmosphere.loop();
		Resources.setMusic("atmosphere");

		v.frameCount = 0;

		// Add initial planets
		for(Planet p : generator.generate()) {
			addObject(p);
		}

		playerShip = new Spaceship(
				"VEKTA I",
				5000,  // Mass
				5,     // Radius
				new PVector(1, 0), // Heading
				new PVector(), // Position
				new PVector(),    // Velocity
				v.color(0, 255, 0),
				0, .1F, 60,  // Control scheme, Speed, and Handling
				100, // Starting ammo
				50 // Starting money
		);
		addObject(playerShip);
	}

	@Override
	public void render() {
		Vekta v = getInstance();
		v.background(0);

		if(!dead) {
			pos = playerShip.getPosition();
			spd = playerShip.getVelocity().mag();
			// Camera follow
			v.camera(pos.x, pos.y, (.07F * spd + .7F) * (v.height / 2F) / tan(PI * 30 / 180) * zoom, pos.x, pos.y, 0F,
					0F, 1F, 0F);
		} else {
			v.camera(lastKnownPosition.x, lastKnownPosition.y, .7F * (v.height / 2F) / tan(PI * 30 / 180) * zoom, lastKnownPosition.x, lastKnownPosition.y, 0F,
					0F, 1F, 0F);
		}
		
		closestObject = null;
		minDistSq = Float.POSITIVE_INFINITY;
		planetCount = 0;
		for(SpaceObject s : objects) {
			if(markedForDeath.contains(s)) {
				continue;
			}

			if(s instanceof Planet) {
				planetCount++;
				float distSq = getDistSq(s.getPosition(), playerShip.getPosition());
				if(distSq < minDistSq) {
					closestObject = s;
					minDistSq = distSq;
				}
			}
			s.update();
			s.applyInfluenceVector(objects);
			for(SpaceObject other : objects) {
				if(s != other) {
					checkCollision(s, other);
					checkCollision(other, s);
				}
			}
			s.draw();
			s.drawTrail();
		}

		objects.removeAll(markedForDeath);
		objects.addAll(markedForAddition);
		markedForDeath.clear();
		markedForAddition.clear();

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
			v.text("Health = " + health + "\nAmmunition = " + ammunition + "\nGold = " + playerShip.getInventory().getMoney(), v.width - 300, v.height - 100);
			// Ship heading indicator
			drawDial("Heading", playerShip.getHeading(), v.width - 370, v.height - 65, 50, v.color(0, 255, 0));
			drawDial("Velocity", playerShip.getVelocity().copy(), v.width - 500, v.height - 65, 50, v.color(0, 255, 0));
			// Text - left
			String closestObjectString;
			if(closestObject == null) {
				v.fill(100, 100, 100);
				closestObjectString = "Closest Object: None in range";
				minDistSq = Integer.MAX_VALUE;
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
				} else {
					closestObjectString = "Closest Object: " + closestObject.getName() + " - " + shortDist + "AU \nSpeed: " + (float)round(closestObject.getVelocity().mag() * 100) / 100 + "\nMass: " + closestMass + " " + closestMassUnit;
				}
				// Closest object arrow
				drawDial("Direction", closestObject.getPosition().sub(pos), 450, v.height - 65, 50, closestObject.getColor());
				v.stroke(closestObject.getColor());
				v.fill(closestObject.getColor());
			}
			v.text(closestObjectString, 50, v.height - 100);
			if(playerShip.isLanding()) {
				//textSize(24);
				v.text(":: Landing Autopilot ::", 50, v.height - 150);
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

	private void checkCollision(SpaceObject a, SpaceObject b) {
		if(a.collidesWith(b)) {
			a.onCollide(b);
		}
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
		shortDist = (float)round(sqrt(minDistSq) * 100) / 100;
		speed = (float)round(spd * 100) / 100;
		ammunition = playerShip.getAmmo();
		position = round(pos.x) + ", " + round(pos.y);
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
		zoom = Vekta.max(.1F, Vekta.min(3, zoom * (1 + amount * .1F)));
	}

	public void setDead() {
		dead = true;
		lastKnownPosition = playerShip.getPosition();
		if(Resources.getMusic() != null) lowPass.process(Resources.getMusic(), 800);
		Resources.stopSound("engine");
		Resources.playSound("death");
	}

	@Override
	public void restart() {
		Vekta.startWorld(new Singleplayer());
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
}
