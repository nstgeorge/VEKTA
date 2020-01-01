package vekta.object.ship;

import processing.core.PVector;
import vekta.world.RenderLevel;
import vekta.object.Countermeasure;
import vekta.object.HomingResponder;

import static vekta.Vekta.*;

public class BossShip extends FighterShip implements HomingResponder {
	private  static final float COUNTERMEASURE_RATE = 1;
	
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
		return tier;
	}

	@Override
	public int chooseAttackTime() {
		return super.chooseAttackTime() / tier;
	}

	@Override
	public void respondIncoming(Damager damager) {
		if(v.chance(COUNTERMEASURE_RATE * getTier() * getWorld().getTimeScale() / v.frameRate)) {
			getWorld().playSound("countermeasure", getPosition());
			PVector velocity = PVector.random2D().mult(getAttackScale())
					.sub(getHeading())
					.add(getVelocity());
			register(new Countermeasure(this, getPosition(), velocity));
		}
	}
}  
