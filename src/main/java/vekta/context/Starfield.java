package vekta.context;

import processing.core.PVector;
import vekta.module.HyperdriveModule;
import vekta.module.Module;
import vekta.object.SpaceObject;
import vekta.object.planet.BlackHole;
import vekta.object.ship.ModularShip;
import vekta.player.Player;
import vekta.situation.BlackHoleSituation;
import vekta.world.World;

import java.io.Serializable;

import static processing.core.PApplet.*;
import static vekta.Vekta.*;

public class Starfield implements Serializable {

	private static final float VELOCITY_SCALE = 0.000005f; // Affects how quickly the stars move relative to player's velocity
	private static final float BLUR_SCALE = 10;           // Affects the blur effect on each star
	private static final float DILATE_SCALE = 0.02f;      // Affects the amount of spatial dilation at higher velocities

	private final BackgroundStar[] stars = new BackgroundStar[200];

	private final World world;

	private PVector shipVelocity;
	private float shipSpeed;
	private float logTimeScale;
	private boolean hyperdrive;
	private PVector blackHoleCoords;

	public Starfield(World world) {
		this.world = world;
		setup();
	}

	private BackgroundStar createStar() {
		return new BackgroundStar(new PVector(v.random(-(float)v.width / 2, (float)v.width / 2), v.random(-(float)v.height / 2, (float)v.height / 2)), v.random(1));
	}

	public void setup() {
		for(int i = 0; i < stars.length; i++) {
			stars[i] = createStar();
		}
	}

	public void update(ModularShip ship) {
		shipVelocity = ship.getVelocity();
		logTimeScale = log(world.getTimeScale());

		hyperdrive = false;
		for(Module module : ship.getModules()) {
			if(module instanceof HyperdriveModule && ((HyperdriveModule)module).isActive()) {
				hyperdrive = true;
				break;
			}
		}

		Player player = ship.getController();
		if(player != null && player.hasAttribute(BlackHoleSituation.class)) {
			SpaceObject orbit = getWorld().findOrbitObject(ship);
			if(orbit instanceof BlackHole) {
				blackHoleCoords = ship.relativePosition(orbit).div(getWorld().getZoom());
			}
		}
		else {
			blackHoleCoords = null;
		}

		if(blackHoleCoords != null) {
			//			shipVelocity.add(ship.getAccelerationReference().copy().mult(-2));
			//			float distSq = blackHoleCoords.magSq();
			//				dir.add(singularityCoords.normalize().div(distSq));
		}

		shipSpeed = shipVelocity.mag();

		for(BackgroundStar star : stars) {
			star.update(ship);
		}
	}

	public void draw(ModularShip ship) {
		update(ship);

		for(int i = 0; i < stars.length; i++) {
			BackgroundStar star = stars[i];
			if(star.shouldReplace()) {
				stars[i] = createStar();
			}
			star.draw(ship);
		}
	}

	private class BackgroundStar {
		private final PVector location;       // Location of the star within the screen
		private final float closeness;        // Closeness to the player - affects parallax. 0: Infinitely far, 1: on the same plane as player

		private PVector velocity;

		public BackgroundStar(PVector location, float closeness) {
			this.location = location;
			this.closeness = closeness;
		}

		public void update(ModularShip ship) {
			velocity = shipVelocity.copy();
			if(!hyperdrive && blackHoleCoords != null) {
				PVector vec = blackHoleCoords.copy().div(2).sub(location);
				float dist = vec.mag();
				velocity = velocity.mult(1 - v.min(1, 2 * v.width / dist)).add(vec.mult(-2e5F / dist));
			}

			location.sub(velocity.copy().mult(closeness * VELOCITY_SCALE * logTimeScale * (hyperdrive ? (3 - location.magSq() / sq(v.width)) : 1)));
		}

		public void draw(ModularShip ship) {

			float x1 = location.x;
			float y1 = location.y;
			float r1 = sqrt(sq(x1) + sq(y1));

			float lineScale = VELOCITY_SCALE * BLUR_SCALE * (hyperdrive ? 3 : 1);

			float x2 = x1 - (velocity.x * lineScale);
			float y2 = y1 - (velocity.y * lineScale);
			float r2 = sqrt(sq(x2) + sq(y2));

			float dot = velocity.dot(x1, y1, 0) / shipSpeed / v.width;

			float d1 = dilate(ship, r1, dot);
			float d2 = dilate(ship, r2, dot);

			int color = hyperdrive ? v.lerpColor(v.color(100), dot > 0 ? v.color(150, 150, 255) : v.color(255, 50, 50), (dot + 1) / 2) : v.color(100);

			//			v.stroke(v.lerpColor(0, 100, closeness));
			v.stroke(v.lerpColor(v.color(0), color, closeness * .5F));
			v.line(x1 * d1, y1 * d1, x2 * d2, y2 * d2);
		}

		private float dilate(ModularShip ship, float r, float dot) {
			return 1 / (1 + shipSpeed * (hyperdrive ? 2 : closeness) * VELOCITY_SCALE * DILATE_SCALE * logTimeScale) - (hyperdrive ? 1 : sq(dot) - 1) * sqrt(r) / sqrt(v.width) + 1;
		}

		public PVector getLocationReference() {
			return location;
		}

		public boolean shouldReplace() {
			if(blackHoleCoords != null && v.chance(sq(100) / distSq(location, blackHoleCoords))) {
				return true;
			}
			return location.x > (float)v.width / 2 + 300 || location.y > (float)v.height / 2 + 200 || location.x < -((float)v.width / 2 + 300) || location.y < -((float)v.height / 2 + 200);
		}
	}
}
