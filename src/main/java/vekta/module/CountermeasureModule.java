package vekta.module;

import processing.core.PVector;
import vekta.KeyBinding;
import vekta.object.Countermeasure;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.register;

public class CountermeasureModule extends WeaponModule {
	private static final float LAUNCH_SPEED = 3;

	private final float efficiency;

	public CountermeasureModule() {
		this(1);
	}

	public CountermeasureModule(float efficiency) {
		this.efficiency = efficiency;
	}

	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public String getName() {
		return "Countermeasure System v" + getEfficiency();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public int getMass() {
		return 1000;
	}

	@Override
	public float getValueScale() {
		return 1.5F * getEfficiency();
	}

	@Override
	public BaseModule createVariant() {
		return new CountermeasureModule(chooseInclusive(1, 3));
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof CountermeasureModule && getEfficiency() > ((CountermeasureModule) other).getEfficiency();
	}

	@Override
	public KeyBinding getFireKey() {
		return KeyBinding.SHIP_DEFEND;
	}

	@Override
	public void fireWeapon() {
		ModularShip ship = getShip();

		if (ship.consumeEnergyImmediate(1 / getEfficiency())) {
			getWorld().playSound("countermeasure", ship.getPosition());
			register(new Countermeasure(
					ship,
					ship.getPosition(),
					ship.getVelocity().add(PVector.random2D().mult(getEfficiency() * LAUNCH_SPEED)).sub(ship.getHeading())));
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Keep those pesky torpedoes away from your ship.");

		info.addKey(getFireKey(), "deploy countermeasure");
	}
}
