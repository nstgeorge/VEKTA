package vekta.context;

import processing.core.PVector;
import vekta.object.ship.ModularShip;
import vekta.world.World;

import java.io.Serializable;

import static processing.core.PApplet.log;
import static vekta.Vekta.v;

public class Starfield implements Serializable {

	private static final int DENSITY = 200;                 // How many stars should be drawn on screen
	private static final float VELOCITY_SCALING = 0.00001f; // Affects how quickly the stars move relative to player's velocity
	private static final float BLUR_SCALING = 10;           // Affects the blur effect on each star

	private static BackgroundStar[] stars;

	private final World world;

	public Starfield(World world) {
		this.world = world;
		setup();
	}

	public void setup() {
		stars = new BackgroundStar[DENSITY];
		for(int i = 0; i < DENSITY; i++) {
			stars[i] = new BackgroundStar(new PVector(v.random(-(float)v.width / 2, (float)v.width / 2), v.random(-(float)v.height / 2, (float)v.height / 2)), v.random(1));
		}
	}

	public void draw(ModularShip playerShip) {
		update(playerShip);
		for(int i = 0; i < DENSITY; i++) {
			BackgroundStar star = stars[i];
			if(star.getLocation().x > (float)v.width / 2 + 300 || star.getLocation().y > (float)v.height / 2 + 200 || star.getLocation().x < -((float)v.width / 2 + 300) || star.getLocation().y < -((float)v.height / 2 + 200)) {
				stars[i] = new BackgroundStar(new PVector(v.random(-(float)v.width / 2, (float)v.width / 2), v.random(-(float)v.height / 2, (float)v.height / 2)), v.random(1));
			}
			star.draw(playerShip.getVelocity());
		}
	}

	public void update(ModularShip playerShip) {
		for(BackgroundStar star : stars) {
			star.update(playerShip.getVelocity());
		}
	}

	private class BackgroundStar {
		private final PVector location;       // Location of the star within the screen
		private final float closeness;        // Closeness to the player - affects parallax. 0: Infinitely far, 1: on the same plane as player

		public BackgroundStar(PVector location, float closeness) {
			this.location = location;
			this.closeness = closeness;
		}

		public void draw(PVector velocity) {
			v.stroke(v.lerpColor(0, 100, closeness));
			v.line(location.x, location.y, location.x - (velocity.x * VELOCITY_SCALING * BLUR_SCALING), location.y - (velocity.y * VELOCITY_SCALING * BLUR_SCALING));
		}

		public void update(PVector velocity) {
			location.sub(velocity.mult(closeness * VELOCITY_SCALING * log(world.getTimeScale())));
		}

		public PVector getLocation() {
			return location;
		}
	}
}
