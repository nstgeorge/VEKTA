package vekta.module;

import vekta.util.InfoGroup;
import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

import static java.lang.Math.abs;

public class RCSModule extends ShipModule {
	private final float turnSpeed;

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
			ship.turn(turn * getTurnSpeed());
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == KeyBinding.SHIP_LEFT) {
			getShip().setTurnControl(-1);
		}
		if(key == KeyBinding.SHIP_RIGHT) {
			getShip().setTurnControl(1);
		}
	}

	@Override
	public void onKeyRelease(KeyBinding key) {
		if(key == KeyBinding.SHIP_LEFT || key == KeyBinding.SHIP_RIGHT) {
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
