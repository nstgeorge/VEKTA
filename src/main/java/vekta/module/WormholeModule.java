package vekta.module;

import processing.core.PVector;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.CustomOption;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.*;

public class WormholeModule extends ShipModule {
	private static final float MIN_DISTANCE = AU_DISTANCE; // Min teleport distance (prevents accidental teleportation)
	private static final float ENERGY = 50;

	private SpaceObject target;

	public WormholeModule() {
	}

	public WormholeModule(SpaceObject target) {
		setTarget(target);
	}

	public boolean hasTarget() {
		return target != null && !target.isDestroyed();
	}

	public SpaceObject getTarget() {
		if(!hasTarget()) {
			target = null;
		}
		return target;
	}

	public void setTarget(SpaceObject target) {
		this.target = target;
	}

	public float getEnergyConsumption() {
		return ENERGY;
	}

	@Override
	public String getName() {
		return "Wormhole Drive (" + (hasTarget() ? getTarget().getName() : "untargeted") + ")";
	}

	@Override
	public ModuleType getType() {
		return ModuleType.HYPERDRIVE;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public Module getVariant() {
		SpaceObject target = v.chance(.5F) ? getWorld().findRandomObject(SpaceObject.class) : null;
		return new WormholeModule(target);
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == KeyBinding.SHIP_TELESCOPE) {
			setTarget(chooseTarget());
			if(getShip().hasController()) {
				getShip().getController().send("Wormhole target "
						+ (hasTarget() ? "updated to " + getTarget().getName() : "cleared"));
			}
		}
		else if(key == KeyBinding.SHIP_HYPERDRIVE) {
			teleport(getShip());
		}
	}

	private SpaceObject chooseTarget() {
		Targeter targeter = (Targeter)getShip().getModule(ModuleType.TARGET_COMPUTER);
		if(targeter != null && targeter.getTarget() != null) {
			return targeter.getTarget();
		}
		return null;
	}

	private void teleport(ModularShip ship) {
		SpaceObject target = hasTarget() ? getTarget() : chooseTarget();
		if(target == null) {
			return;
		}

		if(ship.relativePosition(target).magSq() < MIN_DISTANCE * MIN_DISTANCE) {
			if(ship.hasController()) {
				ship.getController().send("Wormhole Drive target is too close!");
			}
			return;
		}

		float temp = ship.getTemperature();
		if(ship.hasEnergy() && ship.getEnergy() >= getEnergyConsumption()) {
			Resources.playSound("hyperdriveHit");
			ship.consumeEnergyImmediate(getEnergyConsumption());
			ship.setTemperature(temp);
			ship.setVelocity(target.getVelocity());
			ship.setLanding(true);
			PVector offset = PVector.random2D().mult((ship.getRadius() + target.getRadius()) * 2);
			ship.getPositionReference().set(target.getPosition().add(offset));

			Targeter targeter = (Targeter)getShip().getModule(ModuleType.TARGET_COMPUTER);
			if(targeter != null) {
				// Update ship targeter
				targeter.setTarget(target);
			}
		}
		else if(ship.hasController()) {
			ship.getController().send("Not enough energy! (" + (int)ship.getEnergy() + " / " + (int)getEnergyConsumption() + ")");
		}
	}

	@Override
	public void onItemMenu(Item item, Menu menu) {
		onMenu(menu);
	}

	@Override
	public void onMenu(Menu menu) {
		if(!hasTarget()) {
			return;
		}

		if(menu.getHandle() instanceof ObjectMenuHandle && ((ObjectMenuHandle)menu.getHandle()).getSpaceObject() == menu.getPlayer().getShip()) {
			menu.add(new CustomOption("Teleport (" + getTarget().getName() + ")", m -> {
				m.close();
				teleport(m.getPlayer().getShip());
			}));
		}
	}
}
