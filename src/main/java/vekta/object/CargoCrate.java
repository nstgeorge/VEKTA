package vekta.object;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.item.Item;
import vekta.object.ship.ModularShip;

import static processing.core.PConstants.TWO_PI;
import static vekta.Vekta.v;

public class CargoCrate extends SpaceObject {
	private static final float DEF_MASS = 10;
	private static final int DEF_RADIUS = 4;
	private static final float SPIN_SCALE = .1F;

	private final Item item;

	private float angle = v.random(TWO_PI);
	private float spinSpeed = v.random(-1, 1) * SPIN_SCALE;

	public CargoCrate(Item item, PVector position, PVector velocity) {
		super(position, velocity, item.getColor());
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

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		angle += spinSpeed;
	}

	@Override
	public void drawNearby(float r) {
		v.rotate(angle);
		v.rect(0, 0, r, r);
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return !(s instanceof CargoCrate) && super.collidesWith(level, s);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s.isDestroyed()) {
			return;
		}

		destroyBecause(s);
		if(s instanceof ModularShip && ((ModularShip)s).hasController()) {
			Player player = ((ModularShip)s).getController();
			player.send("Picked up: " + getItem().getName())
					.withColor(getItem().getColor());
		}
	}
}  
