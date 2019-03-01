package vekta.object;

import processing.core.PApplet;
import processing.core.PVector;
import vekta.Resources;
import vekta.Vekta;
import vekta.item.Inventory;
import vekta.item.Item;

import static processing.core.PConstants.CLOSE;
import static vekta.Vekta.addObject;
import static vekta.Vekta.getInstance;

public abstract class Ship extends SpaceObject {
	private static final float CRATE_SPEED = 1;

	protected enum SHIP_SHAPE {
		DEFAULT, CARGO_SHIP, FIGHTER
	}
	
	private final String name;
	private final float mass;
	private final float radius;
	protected final PVector heading;
	private final float speed;  // Force of the vector added when engine is on
	private final float turnSpeed; // angular speed when turning

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

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof CargoCrate) {
			// Add item to ship's inventory
			getInventory().add(((CargoCrate)s).getItem());
			Resources.playSound("change"); // TODO: custom pickup sound
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

	protected void drawShip(SHIP_SHAPE shape) {
		float theta = heading.heading() + PApplet.radians(90);
		Vekta v = getInstance();
		v.fill(0);
		v.stroke(getColor());
		v.pushMatrix();
		v.translate(position.x, position.y);
		v.rotate(theta);
		v.beginShape();
		switch (shape) {
			case CARGO_SHIP:
				v.vertex(0, -radius * 2 - 5);
				v.vertex(-radius, -radius * 2);
				v.vertex(-radius, radius * 2);
				v.vertex(radius, radius * 2);
				v.vertex(radius, -radius * 2);
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
}  
