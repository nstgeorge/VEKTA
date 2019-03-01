package vekta.object;

import processing.core.PApplet;
import processing.core.PVector;
import vekta.Vekta;
import vekta.item.Inventory;

import static processing.core.PConstants.TRIANGLES;
import static vekta.Vekta.getInstance;

public abstract class Ship extends SpaceObject {
	private final String name;
	private final float mass;
	private final float radius;
	protected final PVector heading;
	private final float speed;  // Force of the vector added when engine is on
	private final float turnSpeed; // Speed of turning

	private final Inventory inventory = new Inventory();

	public Ship(String name, float mass, float radius, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(position, velocity, color);
		this.name = name;
		this.mass = mass;
		this.radius = radius;
		this.heading = heading;
		this.speed = speed;
		this.turnSpeed = turnSpeed;
	}

	// Draws a nice triangle
	// Shamelessly stolen from https://processing.org/examples/flocking.html
	@Override
	public void draw() {
		// Draw a triangle rotated in the direction of ship
		float theta = heading.heading() + PApplet.radians(90);
		Vekta v = getInstance();
		v.fill(1);
		v.stroke(getColor());
		v.pushMatrix();
		v.translate(position.x, position.y);
		v.rotate(theta);
		v.beginShape(TRIANGLES);
		v.vertex(0, -radius * 2);
		v.vertex(-radius, radius * 2);
		v.vertex(radius, radius * 2);
		v.endShape();
		v.popMatrix();
	}

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
		return heading;
	}

	public float getSpeed() {
		return speed;
	}

	public float getTurnSpeed() {
		return turnSpeed;
	}

	public void accelerate(float amount) {
		if(amount != 0) {
			addVelocity(getHeading().copy().setMag(amount * getSpeed()));
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
}  
