package vekta.object.ship;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.Countermeasure;

import static vekta.Vekta.*;

public class BossShip extends FighterShip {
	private final int tier;

	public BossShip(String name, int tier, PVector heading, PVector position, PVector velocity) {
		super(name, heading, position, velocity, DANGER_COLOR);

		this.tier = tier;
	}

	public int getTier() {
		return tier;
	}

	@Override
	public RenderLevel getDespawnLevel() {
		return RenderLevel.STAR;
	}

	@Override
	public float getRadius() {
		return super.getRadius() * 2;
	}

	@Override
	public float getAttackScale() {
		return super.getAttackScale() * 3;
	}

	@Override
	public int chooseAttackTime() {
		return super.chooseAttackTime() / 3;
	}

	@Override
	public void fireProjectile() {
		if(v.chance(.9F)) {
			super.fireProjectile();
		}
		else {
			getWorld().playSound("countermeasure", getPosition());
			PVector velocity = PVector.random2D().mult(getAttackScale())
					.sub(getHeading())
					.add(getVelocity());
			register(new Countermeasure(this, getPosition(), velocity));
		}
	}
}  
