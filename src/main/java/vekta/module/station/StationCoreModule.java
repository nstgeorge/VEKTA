package vekta.module.station;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.menu.Menu;
import vekta.menu.option.DeployOption;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.ShipModule;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;

import static vekta.Vekta.v;

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

	public int getModuleTypeLimit() {
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
	public boolean isBetter(Module other) {
		return other instanceof StationCoreModule && getTier() > ((StationCoreModule)other).getTier();
	}

	@Override
	public Module getVariant() {
		return new StationCoreModule();
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
	public void onActionMenu(Menu menu) {
		ModularShip ship = menu.getPlayer().getShip();
		menu.add(new DeployOption("New Station", menu.getPlayer(), () -> new SpaceStation(
				getName() + " Station",
				this,
				ship.getPosition().add(ship.getHeading().setMag(ship.getRadius() * 2)),
				ship.getVelocity(),
				PVector.random2D(),
				ship.getColor())));
	}
}
