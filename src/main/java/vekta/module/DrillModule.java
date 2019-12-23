package vekta.module;

import vekta.InfoGroup;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.ExtractMenuButton;
import vekta.menu.option.LootMenuButton;
import vekta.object.RingDebris;
import vekta.object.SpaceObject;
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
	public int getMass() {
		return (int)((getEfficiency() + 2) * 500);
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof DrillModule && getEfficiency() > ((DrillModule)other).getEfficiency();
	}

	@Override
	public Module createVariant() {
		return new DrillModule(chooseInclusive(.5F, 3, .5F));
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();

			if(site.getTerrain().hasFeature("Mineable")) {
				menu.add(new ExtractMenuButton(site, getShip().getInventory(), round(getEfficiency() * 2)));
			}
		}
		else if(menu.getHandle() instanceof SpaceObjectMenuHandle) {
			SpaceObject s = ((SpaceObjectMenuHandle)menu.getHandle()).getSpaceObject();

			if(s instanceof RingDebris) {
				Inventory inv = ((RingDebris)s).getInventory();
				if(inv.itemCount() > 0) {
					// TODO: merge logic with ExtractMenuButton
					menu.add(new LootMenuButton("Extract", getShip().getInventory(), inv));
				}
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Extract natural resources from planets and asteroids.");
	}
}
