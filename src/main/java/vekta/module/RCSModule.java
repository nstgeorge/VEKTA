package vekta.module;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

import static java.lang.Math.abs;

public class RCSModule extends ShipModule {
	private final float turnSpeed;
	private long turnStart;								// Timestamp of when the key was first pressed - used in calculating turn acceleration
	private static final float turnAcceleration = 4.7f; // For now, all RCS modules accelerate at the same rate

	public RCSModule() {
		this(1);
	}

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
	public int getMass() {
		return (int)((getTurnSpeed() + 5) * 100);
	}

	@Override
	public float getValueScale() {
		return .5F * getTurnSpeed();
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof RCSModule && getTurnSpeed() > ((RCSModule)other).getTurnSpeed();
	}

	@Override
	public Module createVariant() {
		return new RCSModule(chooseInclusive(.5F, 3, .1F));
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();
		float turn = ship.getTurnControl();
		if(ship.consumeEnergyOverTime(5 * getTurnSpeed() * abs(turn) * PER_MINUTE)) {
			float time = (System.currentTimeMillis() - turnStart) / 1000.0f;
			ship.turn((turn * getTurnSpeed()) / (float)(1 + Math.pow(Math.E, -turnAcceleration * (time - (5 - turnAcceleration))))/* * ship.getBaseMass() / ship.getMass()*/);
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		turnStart = System.currentTimeMillis();
		if(key == KeyBinding.SHIP_LEFT) {
			getShip().setTurnControl(-1);
		}
		if(key == KeyBinding.SHIP_RIGHT) {
			getShip().setTurnControl(1);
		}
	}

	@Override
	public void onKeyRelease(KeyBinding key) {
		if(key == KeyBinding.SHIP_LEFT && getShip().getTurnControl() == -1 || key == KeyBinding.SHIP_RIGHT && getShip().getTurnControl() == 1) {
			getShip().setTurnControl(0);
		}
	}

	@Override
	public void onMenu(Menu menu) {
		getShip().setTurnControl(0);
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("A must-have for pilots who like to fly in more than one direction.");

		info.addKey(KeyBinding.SHIP_LEFT, "rotate counterclockwise");
		info.addKey(KeyBinding.SHIP_RIGHT, "rotate clockwise");
	}
}
