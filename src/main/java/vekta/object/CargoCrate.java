package vekta.object;

import processing.core.PVector;
import vekta.Vekta;
import vekta.item.Item;

import static vekta.Vekta.*;

public class CargoCrate extends SpaceObject {
	// Default settings
	private static final float DEF_MASS = 50;
	private static final int DEF_RADIUS = 3;
	private static final float SPIN_SCALE = .1F;

	private final Item item;

	private float angle = getInstance().random(2 * PI);
	private float spinSpeed = getInstance().random(-1, 1) * SPIN_SCALE;

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
		Vekta v = getInstance();
		v.stroke(getColor());
		v.fill(0);
		v.pushMatrix();
		v.translate(position.x, position.y);
		v.rotate(angle);
		float r = getRadius();
		v.rect(-r, -r, r, r);
		v.rotate(-angle);
		v.popMatrix();
	}

	@Override
	public boolean shouldDestroy(SpaceObject other) {
		return false;
	}

	@Override
	public void onCollide(SpaceObject s) {
		super.onCollide(s);
		removeObject(this);
	}
}  
