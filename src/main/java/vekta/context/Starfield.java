package vekta.context;

import processing.core.PVector;
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

	private static final float VELOCITY_SCALE = 0.000005F; // Affects how quickly the stars move relative to player's velocity
	private static final float BLUR_SCALE = 10;           // Affects the blur effect on each star
	private static final float DILATE_SCALE = 0.02F;      // Affects the amount of spatial dilation at higher velocities

	private final BackgroundStar[] stars = new BackgroundStar[200];

	private final World world;

	private PVector shipVelocity;
	private float shipSpeed;
	private float logTimeScale;
	private boolean hyperdriving;
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
		hyperdriving = ship.isHyperdriving();
		logTimeScale = log(world.getTimeScale());

		if(hyperdriving) {
			shipVelocity.mult(.1F);
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

		//		if(blackHoleCoords != null) {
		//			shipVelocity.add(ship.getAccelerationReference().copy().mult(-2));
		//			float distSq = blackHoleCoords.magSq();
		//				dir.add(singularityCoords.normalize().div(distSq));
		//		}

		shipSpeed = shipVelocity.mag();

		for(BackgroundStar star : stars) {
			star.update();
		}
	}

	public void draw(ModularShip ship) {
		update(ship);

		for(int i = 0; i < stars.length; i++) {
			BackgroundStar star = stars[i];
			if(star.shouldReplace()) {
				stars[i] = createStar();
			}
			star.draw();
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

		public void update() {
			velocity = shipVelocity.copy();
			if(!hyperdriving && blackHoleCoords != null) {
				PVector vec = blackHoleCoords.copy().div(2).sub(location);
				float dist = vec.mag();
				velocity.mult(1 - v.min(1, 2 * v.width / dist)).add(vec.mult(-3e4F / dist));
			}

			location.sub(velocity.copy().mult(closeness * VELOCITY_SCALE * logTimeScale * (hyperdriving ? (3 - location.magSq() / sq(v.width)) * 2 : 1)));
		}

		public void draw() {

			float x1 = location.x;
			float y1 = location.y;
			float r1 = sqrt(sq(x1) + sq(y1));

			float lineScale = VELOCITY_SCALE * BLUR_SCALE * (hyperdriving ? 3 : 1);

			float x2 = x1 - (velocity.x * lineScale);
			float y2 = y1 - (velocity.y * lineScale);
			float r2 = sqrt(sq(x2) + sq(y2));

			float dot = velocity.dot(x1, y1, 0) / shipSpeed / v.width;

			float c1 = getCurvature(r1, dot);
			float c2 = getCurvature(r2, dot);

			int color = hyperdriving ? v.lerpColor(v.color(150), dot > 0 ? v.color(150, 150, 255) : v.color(255, 50, 50), (dot + 1) / 2) : v.color(100);

			//			v.stroke(v.lerpColor(0, 100, closeness));
			v.stroke(v.lerpColor(v.color(0), color, closeness * .7F));
			v.line(x1 * c1, y1 * c1, x2 * c2, y2 * c2);
		}

		private float getCurvature(float r, float dot) {
			return 1 / (1 + shipSpeed * VELOCITY_SCALE * (hyperdriving ? 2 : closeness) * logTimeScale * DILATE_SCALE) - (hyperdriving ? 1 : sq(dot) - 1) * sqrt(r) / sqrt(v.width) + 1;
		}

		public PVector getLocationReference() {
			return location;
		}

		public boolean shouldReplace() {
			if(blackHoleCoords != null && v.chance(sq(50) / distSq(location, blackHoleCoords))) {
				return true;
			}
			return location.x > (float)v.width / 2 + 300 || location.y > (float)v.height / 2 + 200 || location.x < -((float)v.width / 2 + 300) || location.y < -((float)v.height / 2 + 200);
		}
	}
}
