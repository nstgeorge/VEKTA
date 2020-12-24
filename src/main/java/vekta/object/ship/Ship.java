package vekta.object.ship;

import processing.core.PVector;
import vekta.Renameable;
import vekta.item.Inventory;
import vekta.item.InventoryListener;
import vekta.item.Item;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.ShipKnowledge;
import vekta.menu.Menu;
import vekta.object.CargoCrate;
import vekta.object.Shockwave;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.terrain.LandingSite;
import vekta.world.RenderLevel;

import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.*;

public abstract class Ship extends SpaceObject implements Renameable, InventoryListener, Damageable {
	private static final float FADE_AMOUNT = 10; // Reticle zoom fading factor
	private static final float CRATE_SPEED = 10; // Speed of item drops when destroyed
	private static final int DEPART_FRAMES = 100; // Number of seconds to wait before docking/landing again
	private static final float BLINK_DECAY_RATE = 1.5F; // Blink color decay rate
	private static final float REENTRY_ANIMATION_DIST = .2f;    // Distance from the atmosphere threshold at which the re-entry effect begins and ends
	private static final float REENTRY_ANIMATION_MAX = 1;    // Ratio between planet radius / atmosphere altitude with maximum re-entry strength
	private static final float GLIDE_DAMPEN_RATE = .01f;    // Aerodynamic velocity dampening

	private String name;
	private final float speed;  // Force induced when engine is on
	private final float turnSpeed; // Rotational speed when turning

	protected final PVector heading = new PVector();
	private final Inventory inventory = new Inventory(this);

	private int blinkColor;

	private TerrestrialPlanet nearestPlanet;
	private boolean gliding;

	private transient SpaceObject dock;
	private transient int departTime; // Dock/land debounce frame

	public Ship(String name, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(position, velocity, color);

		this.name = name;
		this.heading.set(heading);
		this.speed = speed;
		this.turnSpeed = turnSpeed;

		setTemperatureKelvin(getOptimalTemperature());
	}

	public float getOptimalTemperature() {
		return 23;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public RenderLevel getDespawnLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	public PVector getHeading() {
		return getHeadingReference().copy();
	}

	public PVector getHeadingReference() {
		return heading;
	}

	public void setHeading(PVector heading) {
		this.heading.set(heading).normalize();
	}

	public float getSpeed() {
		return speed;
	}

	public float getTurnSpeed() {
		return turnSpeed;
	}

	public boolean isDocked() {
		return getDock() != null;
	}

	public SpaceObject getDock() {
		return dock;
	}

	public boolean isDockable(SpaceObject s) {
		return v.frameCount - departTime >= DEPART_FRAMES;
	}

	public final void dock(SpaceObject s) {
		dock = s;
		doDock(s);
	}

	public final void undock() {
		SpaceObject dock = getDock();
		if(dock != null) {
			PVector offset = getPosition().sub(dock.getPosition());
			setVelocity(dock.getVelocity().add(offset.mult(.01F)));
			this.dock = null;
			onDepart(dock);
		}
		departTime = v.frameCount; // Calls to undock() start a docking/landing debounce
	}

	public void accelerate(float amount) {
		accelerate(amount, heading);
	}

	public void accelerate(float amount, PVector direction) {
		if(amount != 0) {
			addVelocity(direction.copy().setMag(amount * getSpeed() * getWorld().getTimeScale()));
		}
	}

	public void turn(float amount) {
		heading.rotate(amount * getTurnSpeed() * DEG_TO_RAD);
	}

	public TerrestrialPlanet getNearestPlanet() {
		return nearestPlanet;
	}

	public boolean isGliding() {
		return gliding;
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return super.collidesWith(level, s);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof CargoCrate) {
			// Add item to ship's inventory
			getInventory().add(((CargoCrate)s).getItem());
		}
		else if(s != dock && s instanceof Ship && isDockable(s) && ((Ship)s).isDockable(this)) {
			// Dock ships to each other
			dock(s);
			((Ship)s).dock(this);
		}
	}

	@Override
	public boolean damage(float amount, Damager damager) {
		if(damager.getParentObject().getColor() == getColor()) {
			return false;
		}

		blink(v.color(255));
		if(amount > 0) {
			destroyBecause(damager.getParentObject());
			return true;
		}
		return false;
	}

	@Override
	public void onDestroyed(SpaceObject s) {
		// Add shockwaves
		for(int i = 0; i < 3; i++) {
			register(new Shockwave(this, sq(v.random(.5F, 1)), (int)v.random(10, 60), v.chance(.5F) ? 255 : getColor()));
		}

		// Add cargo drops
		for(Item item : getInventory()) {
			register(new CargoCrate(item, getPosition(), PVector.random2D().setMag(v.random(CRATE_SPEED))));
		}

		// Inherit behavior
		super.onDestroyed(s);
	}

	public void blink(int blinkColor) {
		this.blinkColor = blinkColor;
	}

	@Override
	public void draw(RenderLevel level, float r) {
		//		drawReentryEffect(r); // Currently called in `PlayerShip::drawNearby(..)`
		blinkColor = v.lerpColor(blinkColor, getColor(), BLINK_DECAY_RATE / v.frameRate);
		v.stroke(blinkColor);
		drawShipMarker(level, r);
		super.draw(level, r);
	}

	/**
	 * Calculates the drag coefficient of the spacecraft for a given angle of attack.
	 *
	 * @param angleOfAttack Angle between heading and relative velocity
	 * @return The corresponding drag coefficient
	 */
	public float getDragCoefficient(float angleOfAttack) {
		// TODO: airbrake module
		// Increases drag when perpendicular to velocity or flying backwards
		return .01F * (1 + abs(sin(angleOfAttack) * 2) + abs(sin(angleOfAttack / 2) * 1.5F));
	}

	@Override
	public boolean shouldIgnoreGravity(SpaceObject object) {
		return gliding && object == nearestPlanet;
	}

	@Override
	public void update(RenderLevel level) {
		nearestPlanet = null;
		float bestDistSq = Float.MAX_VALUE;
		for(TerrestrialPlanet planet : getWorld().findObjects(TerrestrialPlanet.class)) {
			float distSq = relativePosition(planet).magSq();
			if(distSq < bestDistSq) {
				bestDistSq = distSq;
				nearestPlanet = planet;
			}
		}
		if(nearestPlanet != null) {
			float distSq = relativePosition(nearestPlanet).magSq();
			gliding = distSq <= sq(nearestPlanet.getAtmosphereRadius());
			if(gliding) {
				PVector relVelocity = relativeVelocity(nearestPlanet);

				float dot = relVelocity.dot(getHeading().rotate(HALF_PI));
				PVector dampen = relVelocity.copy().setMag(abs(dot)); // Dampen perpendicular motion
				PVector redirect = relVelocity.copy().rotate(90).setMag(dot); // Convert perpendicular to forward

				//				float surfaceProgress = 1 - (sqrt(distSq) / (nearestPlanet.getAtmosphereRadius() - nearestPlanet.getRadius()));

				addVelocity(dampen.sub(redirect).mult(GLIDE_DAMPEN_RATE/* * surfaceProgress*/ * min(10, getWorld().getTimeScale()))); // Apply aerodynamics
			}
		}
		super.update(level);
	}

	@Override
	public void drawTrail(float scale) {
		if(isGliding()) {
			v.stroke(getColor(), .2f);/////////////////////////
		}

		super.drawTrail(scale);
	}

	public void drawReentryEffect(float r) {
		if(nearestPlanet == null || nearestPlanet.getAtmosphereDensity() == 0) {
			return;
		}

		float planetDist = getPosition().dist(nearestPlanet.getPosition());
		float boundaryAbsDist = Math.abs(planetDist - nearestPlanet.getAtmosphereRadius());
		float animationDist = nearestPlanet.getRadius() * REENTRY_ANIMATION_DIST;

		if(boundaryAbsDist > animationDist /*|| !getRenderLevel().isVisibleTo(level) */) {
			return;
		}

		float relVelocityAngle = nearestPlanet.relativeVelocity(this).heading();

		int numberOfArcs = 10;
		int startColor = v.color(255, 255, 255);
		int endColor = v.color(252, 86, 3);

		float strength = ((boundaryAbsDist - animationDist) / animationDist)
				* min(1, (nearestPlanet.getAtmosphereRadius() / nearestPlanet.getRadius() - 1) / REENTRY_ANIMATION_MAX);

		v.rotate(relVelocityAngle);
		for(int i = 1; i <= numberOfArcs; i++) {
			float offset = PI * (v.random(.8f) * strength * i) / numberOfArcs;

			v.noFill();
			v.stroke(v.lerpColor(startColor, endColor, (float)i / numberOfArcs));
			v.strokeWeight(2);
			v.arc((numberOfArcs - i) * 2 * v.random(.9f, 1.1f), 0, r * 5, (r * 3 + (Math.abs(getVelocityReference().heading() % PI - getHeadingReference().heading() % PI))), offset, -offset, OPEN);
		}
		v.rotate(-relVelocityAngle);

		v.stroke(v.lerpColor(getColor(), endColor, sqrt(abs(strength))));
	}

	public void drawShipMarker(RenderLevel level, float r) {
		// Fade marker near ship level
		boolean fading = RenderLevel.SHIP.isVisibleTo(level);
		if(fading) {
			float radius = getRadius();
			v.stroke(v.lerpColor(0, getColor(), radius / max(radius, r * FADE_AMOUNT)));
		}
		drawMarker();
		if(fading) {
			v.stroke(getColor());
		}
	}

	protected void drawShip(float r, ShipModelType shape) {
		v.strokeWeight(2f);
		v.color(getColor());
		float theta = heading.heading() + HALF_PI;
		v.rotate(theta);
		v.beginShape();
		switch(shape) {
		case CARGO_SHIP:
			v.vertex(0, -r * 1.35F);
			v.vertex(-r / 2, -r);
			v.vertex(-r / 2, r);
			v.vertex(r / 2, r);
			v.vertex(r / 2, -r);
			break;
		case FIGHTER:
			v.vertex(0, -r * 2);
			// Draw left spike
			v.vertex(-r, r * 2);
			v.vertex(-r, -r / 3.0F);
			v.vertex(-r, r * 2);
			// Draw right spike
			v.vertex(r, r * 2);
			v.vertex(r, -r / 3.0F);
			v.vertex(r, r * 2);
			break;
		default:
			v.vertex(0, -r * 2);
			v.vertex(-r, r * 2);
			v.vertex(r, r * 2);
			break;
		}
		v.endShape(CLOSE);
	}

	// Named Ship::doLand() to distinguish from PlayerListener::onLand()
	public void doLand(LandingSite site) {
		site.takeoff();
	}

	// Named Ship::doDock() to distinguish from PlayerListener::onDock()
	public void doDock(SpaceObject obj) {
		undock();
	}

	public void setupDockingMenu(Menu menu) {
	}

	public void onDepart(SpaceObject obj) {
	}

	@Override
	public void observe(ObservationLevel level, Player player) {
		super.observe(level, player);

		player.addKnowledge(new ShipKnowledge(level, this));

		if(level == ObservationLevel.OWNED) {
			setPersistent(true);
		}
	}

	// TODO: convert to ShipModel class and implement as subclasses
	protected enum ShipModelType {
		DEFAULT,
		CARGO_SHIP,
		FIGHTER,
	}
}
