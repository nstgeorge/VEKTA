package vekta.module.station;

import static vekta.Vekta.v;

import processing.core.PVector;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.DeployButton;
import vekta.module.BaseModule;
import vekta.module.ModuleType;
import vekta.module.ShipModule;
import vekta.object.ship.ModularShip;
import vekta.object.ship.Ship;
import vekta.object.ship.SpaceStation;
import vekta.util.InfoGroup;
import vekta.world.RenderLevel;

public class StationCoreModule extends ShipModule {
	private final int tier;

	public StationCoreModule() {
		this(1);
	}

	public StationCoreModule(int tier) {
		this.tier = tier;
	}

	public int getTier() {
		return tier;
	}

	public int getPartLimit() {
		return 10 * getTier();
	}

	public int getPartLimitPerType() {
		return getTier();
	}

	@Override
	public String getName() {
		return "Station Core v" + getTier();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.CORE;
	}

	@Override
	public int getMass() {
		return 5000 * getTier();
	}

	@Override
	public float getValueScale() {
		return 2 * getTier();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof StationCoreModule && getTier() > ((StationCoreModule) other).getTier();
	}

	@Override
	public BaseModule createVariant() {
		return new StationCoreModule(chooseInclusive(1, 3));
	}

	@Override
	public boolean isApplicable(ModularShip ship) {
		return ship instanceof SpaceStation;
	}

	@Override
	public void draw(RenderLevel dist, float tileSize) {
		v.rect(0, 0, tileSize, tileSize);
	}

	@Override
	public void onItemMenu(Item item, Menu menu) {
		Ship ship = menu.getPlayer().getShip();
		if (menu.getHandle() instanceof SpaceObjectMenuHandle
				&& ((SpaceObjectMenuHandle) menu.getHandle()).getSpaceObject() == ship) {
			menu.add(new DeployButton("New Station [v" + getTier() + "]", item, () -> new SpaceStation(
					ship.getName() + " Station",
					this,
					ship.getPosition().add(ship.getHeading().setMag(ship.getRadius() * 2)),
					ship.getVelocity(),
					PVector.random2D(),
					ship.getColor())));
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("This unsuspecting cube contains everything needed to build a modern space station.");
	}
}
