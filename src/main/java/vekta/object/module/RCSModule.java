package vekta.object.module;

import vekta.object.ModularShip;

import static java.lang.Math.abs;

public class RCSModule extends ShipModule {
	private final float turnSpeed;

	public RCSModule(float turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	public float getTurnSpeed() {
		return turnSpeed;
	}

	@Override
	public String getName() {
		return "RCS v" + getTurnSpeed();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.RCS;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof RCSModule && getTurnSpeed() > ((RCSModule)other).getTurnSpeed();
	}

	@Override
	public Module getVariant() {
		return new RCSModule(chooseInclusive(1, 3));
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();
		float turn = ship.getTurnControl();
		if(ship.consumeEnergy(5 * getTurnSpeed() * abs(turn) * PER_MINUTE)) {
			ship.turn(turn * getTurnSpeed());
		}
	}

	@Override
	public void onKeyPress(char key) {
		if(key == 'a') {
			getShip().setTurnControl(-1);
		}
		if(key == 'd') {
			getShip().setTurnControl(1);
		}
	}

	@Override
	public void onKeyRelease(char key) {
		if(key == 'a' || key == 'd') {
			getShip().setTurnControl(0);
		}
	}
}
