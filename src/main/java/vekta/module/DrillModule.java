package vekta.module;

import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.ExtractMenuOption;
import vekta.terrain.LandingSite;

import static processing.core.PApplet.round;

public class DrillModule extends ShipModule {
	private final float efficiency;

	public DrillModule() {
		this(1);
	}

	public DrillModule(float efficiency) {
		this.efficiency = efficiency;
	}

	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public String getName() {
		return "Mining Drill v" + getEfficiency();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.DRILL;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof DrillModule && getEfficiency() > ((DrillModule)other).getEfficiency();
	}

	@Override
	public Module getVariant() {
		return new DrillModule(chooseInclusive(.5F, 3, .5F));
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();

			if(site.getTerrain().hasFeature("Mineable")) {
				menu.add(new ExtractMenuOption(site, getShip().getInventory(), round(getEfficiency() * 2)));
			}
		}
	}
}
