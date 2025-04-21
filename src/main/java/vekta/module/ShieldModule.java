package vekta.module;

import static vekta.Vekta.getWorld;

import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

public class ShieldModule extends ShipModule {
	private static final float BASE_DAMAGE_ENERGY = 30;

	private final float efficiency;

	public ShieldModule() {
		this(1);
	}

	public ShieldModule(float efficiency) {
		this.efficiency = efficiency;
	}

	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public String getName() {
		return "Deflector Shield v" + getEfficiency();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.SHIELD;
	}

	@Override
	public int getMass() {
		return 3000;
	}

	@Override
	public float getValueScale() {
		return 2 * getEfficiency();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof ShieldModule && getEfficiency() > ((ShieldModule) other).getEfficiency();
	}

	@Override
	public BaseModule createVariant() {
		return new ShieldModule(chooseInclusive(1, 3, .1F));
	}

	@Override
	public void onDamageShip(ModularShip.DamageAttempt attempt) {
		ModularShip ship = getShip();
		if (ship.consumeEnergyImmediate(BASE_DAMAGE_ENERGY / getEfficiency())) {
			getWorld().addCameraImpact(.1F);
			getWorld().playSound("deflection", ship.getPosition());
			attempt.setAmount(0);
			SpaceObject projectile = attempt.getDamager().getAttackObject();
			projectile.setVelocity(ship.getVelocity()
					.add(projectile.relativePosition(ship).setMag(-projectile.relativeVelocity(ship).mag())));
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Deflect incoming projectiles with a powerful electromagnetic field.");
	}
}
