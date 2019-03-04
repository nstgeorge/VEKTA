package vekta.object;

import processing.core.PVector;
import vekta.item.Item;

import static vekta.Vekta.PI;
import static vekta.Vekta.v;

public class CargoCrate extends SpaceObject {
	// Default settings
	private static final float DEF_MASS = 10;
	private static final int DEF_RADIUS = 4;
	private static final float SPIN_SCALE = .1F;

	private final Item item;

	private float angle = v.random(2 * PI);
	private float spinSpeed = v.random(-1, 1) * SPIN_SCALE;

	public CargoCrate(Item item, PVector position, PVector velocity) {
		super(position, velocity, item.getType().getColor());
		this.item = item;
	}

	@Override
	public String getName() {
		return "Cargo Crate";
	}

	@Override
	public float getMass() {
		return DEF_MASS;
	}

	@Override
	public float getRadius() {
		return DEF_RADIUS;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public void onUpdate() {
		angle += spinSpeed;
	}

	@Override
	public void draw() {
		v.stroke(getColor());
		v.fill(0);
		v.pushMatrix();
		v.translate(position.x, position.y);
		v.rotate(angle);
		float r = getRadius();
		v.rect(0, 0, r, r);
		v.popMatrix();
	}

	@Override
	public void onCollide(SpaceObject s) {
		destroyBecause(s);
	}
}  
