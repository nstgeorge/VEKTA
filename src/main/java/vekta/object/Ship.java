package vekta.object;

import processing.core.PApplet;
import processing.core.PVector;
import vekta.Resources;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.terrain.LandingSite;

import static processing.core.PConstants.CLOSE;
import static vekta.Vekta.*;

public abstract class Ship extends SpaceObject {
	private static final float CRATE_SPEED = 1;
	private static final float MAX_DOCKING_SPEED = 2;

	private final String name;
	private final float mass;
	private final float radius;
	protected final PVector heading;
	private final float speed;  // Force of the vector added when engine is on
	private final float turnSpeed; // Angular speed when turning

	private final Inventory inventory = new Inventory();

	private SpaceObject dock;

	public Ship(String name, float mass, float radius, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(position, velocity, color);
		this.name = name;
		this.mass = mass;
		this.radius = radius;
		this.heading = heading;
		this.speed = speed;
		this.turnSpeed = turnSpeed;
	}

	public abstract void draw();

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getMass() {
		return mass;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	public PVector getHeading() {
		return heading.copy();
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

	public void dock(SpaceObject s) {
		dock = s;
		onDock(s);
	}

	public void undock() {
		if(getDock() != null) {
			setVelocity(getDock().getVelocity());
			PVector offset = getPosition().sub(getDock().getPosition());
			position.add(offset.setMag(getRadius() * 2 + getDock().getRadius() - offset.mag()));
			dock = null;
		}
	}

	/**
	 * Check whether this ship is attempting to land.
	 */
	public abstract boolean isLanding();

	/**
	 * Get the current thrust (usually either -1 or 1)
	 */
	public abstract float getThrustControl();

	/**
	 * Get the current turning speed (usually either -1 or 1)
	 */
	public abstract float getTurnControl();

	public void accelerate(float amount) {
		accelerate(amount, heading);
	}

	public void accelerate(float amount, PVector direction) {
		if(amount != 0) {
			addVelocity(direction.copy().setMag(amount * getSpeed()));
		}
	}

	public void turn(float amount) {
		heading.rotate(amount * getTurnSpeed() / 360);
	}

	@Override
	public boolean collidesWith(SpaceObject s) {
		// TODO: generalize, perhaps by adding SpaceObject::getParent(..) and turnSpeed this case in SpaceObject
		return !(s instanceof Projectile && ((Projectile)s).getParent() == this) && super.collidesWith(s);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof CargoCrate) {
			// Add item to ship's inventory
			getInventory().add(((CargoCrate)s).getItem());
			Resources.playSound("change"); // TODO: custom pickup sound
		}
		else if(s instanceof Ship) {
			Ship ship = (Ship)s;
			if(ship.getVelocity().sub(velocity).magSq() <= MAX_DOCKING_SPEED * MAX_DOCKING_SPEED) {
				// Board ship
				ship.dock(this);
				return;
			}
		}
		super.onCollide(s);
	}

	@Override
	public void onDestroy(SpaceObject s) {
		for(Item item : getInventory()) {
			addObject(new CargoCrate(item, getPosition(), PVector.random2D().setMag(getInstance().random(CRATE_SPEED))));
		}
		super.onDestroy(s);
	}

	protected void drawShip(ShipModelType shape) {
		float theta = heading.heading() + PApplet.radians(90);
		v.fill(0);
		v.stroke(getColor());
		v.pushMatrix();
		v.translate(position.x, position.y);
		v.rotate(theta);
		v.beginShape();
		switch(shape) {
		case CARGO_SHIP:
			v.vertex(0, -radius * 1.35F);
			v.vertex(-radius / 2, -radius);
			v.vertex(-radius / 2, radius);
			v.vertex(radius / 2, radius);
			v.vertex(radius / 2, -radius);
			break;
		case FIGHTER:
			v.vertex(0, -radius * 2);
			// Draw left spike
			v.vertex(-radius, radius * 2);
			v.vertex(-radius, -radius / 3.0F);
			v.vertex(-radius, radius * 2);
			// Draw right spike
			v.vertex(radius, radius * 2);
			v.vertex(radius, -radius / 3.0F);
			v.vertex(radius, radius * 2);
			break;
		default:
			v.vertex(0, -radius * 2);
			v.vertex(-radius, radius * 2);
			v.vertex(radius, radius * 2);
			break;
		}
		v.endShape(CLOSE);
		v.popMatrix();
	}

	public void onLand(LandingSite site) {
		site.takeoff();
	}

	public void onDock(SpaceObject obj) {
		undock();
	}

	protected enum ShipModelType {
		DEFAULT,
		CARGO_SHIP,
		FIGHTER,
	}
}
