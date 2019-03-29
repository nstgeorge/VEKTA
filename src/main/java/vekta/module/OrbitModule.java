package vekta.module;

import processing.core.PVector;
import vekta.Counter;
import vekta.InfoGroup;
import vekta.object.SpaceObject;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.getWorld;

public class OrbitModule extends ShipModule {
	private static final float ADJUST_FACTOR = 1e-6F;

	private final float speed;

	private final Counter retargetCt = new Counter(100).randomize();

	private SpaceObject orbit;

	public OrbitModule() {
		this(1);
	}

	public OrbitModule(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public String getName() {
		return "Orbit Stabilizer v" + getSpeed();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public int getMass() {
		return (int)((getSpeed() + 2) * 500);
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof OrbitModule && getSpeed() > ((OrbitModule)other).getSpeed();
	}

	@Override
	public Module getVariant() {
		return new OrbitModule(chooseInclusive(.1F, 2, .1F));
	}

	@Override
	public void onUpdate() {
		if(!getShip().isLanding() && getShip().consumeEnergyOverTime(10 * getSpeed() * PER_MINUTE)) {
			if(retargetCt.cycle()) {
				orbit = getWorld().findOrbitObject(getShip());
			}

			if(orbit != null) {
				PVector velocity = WorldGenerator.getOrbitVelocity(orbit, getShip());
				getShip().addVelocity(velocity.sub(getShip().getVelocity()).mult(ADJUST_FACTOR * getSpeed() * getWorld().getTimeScale()));
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Automatically stabilize your orbit around a star or planet.");
	}
}
