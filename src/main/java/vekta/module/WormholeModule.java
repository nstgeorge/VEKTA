package vekta.module;

import static vekta.Vekta.AU_DISTANCE;
import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

import processing.core.PVector;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.CustomButton;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

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
		if (!hasTarget()) {
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
	public int getMass() {
		return 700;
	}

	@Override
	public float getValueScale() {
		return 1;
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return false;
	}

	@Override
	public BaseModule createVariant() {
		SpaceObject target = v.chance(.5F) ? getWorld().findRandomObject(SpaceObject.class) : null;
		return new WormholeModule(target);
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if (key == KeyBinding.SHIP_SCAN) {
			setTarget(getShip().findNavigationTarget());
			if (getShip().hasController()) {
				getShip().getController().send("Wormhole target "
						+ (hasTarget() ? "updated to " + getTarget().getName() : "cleared"));
			}
		} else if (key == KeyBinding.SHIP_HYPERDRIVE) {
			teleport(getShip());
		}
	}

	private void teleport(ModularShip ship) {
		SpaceObject target = hasTarget() ? getTarget() : ship.findNavigationTarget();
		if (target == null) {
			return;
		}

		if (ship.relativePosition(target).magSq() < MIN_DISTANCE * MIN_DISTANCE) {
			if (ship.hasController()) {
				ship.getController().send("Wormhole Drive target is too close!");
			}
			return;
		}

		float temp = ship.getTemperatureKelvin();
		if (ship.hasEnergy() && ship.getEnergy() >= getEnergyConsumption()) {
			Resources.playSound("hyperdriveHit");
			ship.consumeEnergyImmediate(getEnergyConsumption());
			ship.setTemperatureKelvin(temp);
			ship.setVelocity(target.getVelocity());
			ship.setLanding(true);
			PVector offset = PVector.random2D().mult((ship.getRadius() + target.getRadius()) * 2);
			ship.getPositionReference().set(target.getPosition().add(offset));

			// Update ship targeter
			ship.setNavigationTarget(target);
		} else if (ship.hasController()) {
			ship.getController()
					.send("Not enough energy! (" + (int) ship.getEnergy() + " / " + (int) getEnergyConsumption() + ")");
		}
	}

	@Override
	public void onItemMenu(Item item, Menu menu) {
		onMenu(menu);
	}

	@Override
	public void onMenu(Menu menu) {
		if (!hasTarget()) {
			return;
		}

		if (menu.getHandle() instanceof SpaceObjectMenuHandle
				&& ((SpaceObjectMenuHandle) menu.getHandle()).getSpaceObject() == menu.getPlayer().getShip()) {
			menu.add(new CustomButton("Teleport (" + getTarget().getName() + ")", m -> {
				m.close();
				teleport(m.getPlayer().getShip());
			}));
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription(
				"We can't find the scientists who invented them, but it's become the latest trend in instant transportation.");

		info.addDescription("Store a target in your inventory for easy access, or equip to teleport on-the-fly.");

		info.addKey(KeyBinding.SHIP_SCAN, "store target");
		info.addKey(KeyBinding.SHIP_HYPERDRIVE, "teleport");
		info.addKey(KeyBinding.SHIP_MENU, "teleport to stored location(s)");
	}
}
