package vekta.module;

import static com.jogamp.opengl.math.FloatUtil.abs;
import static vekta.Vekta.DEVICE;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

public class EngineModule extends ShipModule {
	private final float speed;

	public EngineModule() {
		this(1);
	}

	public EngineModule(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public String getName() {
		return "Engine v" + getSpeed();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.ENGINE;
	}

	@Override
	public int getMass() {
		return 5000;
	}

	@Override
	public float getValueScale() {
		return 1.25F * getSpeed();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof EngineModule && getSpeed() > ((EngineModule) other).getSpeed();
	}

	@Override
	public BaseModule createVariant() {
		return new EngineModule(chooseInclusive(.5F, 3, .1F));
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();
		float thrust = ship.getThrustControl();
		if (ship.consumeEnergyOverTime(20 * getSpeed() * abs(thrust) * PER_MINUTE)) {
			ship.accelerate(thrust * getSpeed()/* * ship.getBaseMass() / ship.getMass() */);
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if (key == KeyBinding.SHIP_FORWARD) {
			getShip().setThrustControl(1);
		}
		if (key == KeyBinding.SHIP_BACKWARD) {
			getShip().setThrustControl(-1);
		}
	}

	@Override
	public void onKeyRelease(KeyBinding key) {
		if (key == KeyBinding.SHIP_FORWARD || key == KeyBinding.SHIP_BACKWARD) {
			getShip().setThrustControl(0);
		}
	}

	@Override
	public void onAnalogKeyPress(float value) {
		if (DEVICE != null && DEVICE.isConnected()) {
			getShip().setThrustControl(value);
		}
	}

	@Override
	public void onMenu(Menu menu) {
		getShip().setThrustControl(0);
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Behold, the world's most efficient spacecraft engine.");

		info.addKey(KeyBinding.SHIP_FORWARD, "accelerate forward");
		info.addKey(KeyBinding.SHIP_BACKWARD, "accelerate backward");
	}
}
