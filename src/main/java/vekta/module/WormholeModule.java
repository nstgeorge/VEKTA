package vekta.module;

import processing.core.PVector;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class WormholeModule extends ShipModule {
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

	public SpaceObject findTarget() {
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
		return "Wormhole Drive (" + (hasTarget() ? findTarget().getName() : "untargeted") + ")";
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
			Targeter targeter = (Targeter)getShip().getModule(ModuleType.TARGET_COMPUTER);
			if(targeter != null && targeter.getTarget() != null) {
				setTarget(targeter.getTarget());
				if(getShip().hasController()) {
					getShip().getController().send("Wormhole target "
							+ (hasTarget() ? "updated to " + findTarget().getName() : "cleared"));
				}
			}
		}
		else if(key == KeyBinding.SHIP_HYPERDRIVE) {
			teleport(getShip());
		}
	}

	public void teleport(ModularShip ship) {
		if(!hasTarget()) {
			return;
		}

		float temp = ship.getTemperature();
		if(ship.hasEnergy() && ship.getEnergy() >= getEnergyConsumption()) {
			ship.consumeEnergy(getEnergyConsumption());
			ship.setTemperature(temp);
			PVector offset = PVector.random2D().mult((ship.getRadius() + target.getRadius()) * 2);
			ship.getPositionReference().set(target.getPosition().add(offset));
			ship.setVelocity(target.getVelocity());
			Resources.playSound("hyperdriveHit");
			getShip().setLanding(true);
		}
		else if(ship.hasController()) {
			ship.getController().send("Not enough energy! (" + (int)getShip().getEnergy() + " / " + (int)getEnergyConsumption() + ")");
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
			menu.add(new BasicOption("Teleport (" + findTarget().getName() + ")", m -> {
				m.close();
				teleport(m.getPlayer().getShip());
			}));
		}
	}
}
