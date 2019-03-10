package vekta.module;

import vekta.KeyBinding;
import vekta.object.ship.ModularShip;

import static com.jogamp.opengl.math.FloatUtil.abs;

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
	public boolean isBetter(Module other) {
		return other instanceof EngineModule && getSpeed() > ((EngineModule)other).getSpeed();
	}

	@Override
	public Module getVariant() {
		return new EngineModule(chooseInclusive(.5F, 3, .1F));
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();
		float thrust = ship.getThrustControl();
		if(ship.consumeEnergy(20 * getSpeed() * abs(thrust) * PER_MINUTE)) {
			ship.accelerate(thrust * getSpeed());
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == KeyBinding.SHIP_FORWARD) {
			getShip().setThrustControl(1);
		}
		if(key == KeyBinding.SHIP_BACKWARD) {
			getShip().setThrustControl(-1);
		}
	}

	@Override
	public void onKeyRelease(KeyBinding key) {
		if(key == KeyBinding.SHIP_FORWARD || key == KeyBinding.SHIP_BACKWARD) {
			getShip().setThrustControl(0);
		}
	}
}
