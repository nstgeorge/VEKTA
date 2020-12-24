package vekta.module;

import vekta.menu.handle.LocationMenuHandle;
import vekta.terrain.location.Location;
import vekta.terrain.location.MiningLocation;
import vekta.util.InfoGroup;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.ExtractMenuButton;
import vekta.menu.option.LootMenuButton;
import vekta.object.RingDebris;
import vekta.object.SpaceObject;

public class MiningModule extends ShipModule {
	private final float efficiency;

	public MiningModule() {
		this(1);
	}

	public MiningModule(float efficiency) {
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
		return ModuleType.MINING;
	}

	@Override
	public int getMass() {
		return (int)((getEfficiency() + 2) * 500);
	}

	@Override
	public float getValueScale() {
		return 2 * getEfficiency();
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof MiningModule && getEfficiency() > ((MiningModule)other).getEfficiency();
	}

	@Override
	public Module createVariant() {
		return new MiningModule(chooseInclusive(.5F, 3, .5F));
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LocationMenuHandle) {
			Location location = ((LocationMenuHandle)menu.getHandle()).getLocation();

			if(location instanceof MiningLocation) {
				menu.add(new ExtractMenuButton((MiningLocation)location, getEfficiency()));
			}
		}
		else if(menu.getHandle() instanceof SpaceObjectMenuHandle) {
			SpaceObject s = ((SpaceObjectMenuHandle)menu.getHandle()).getSpaceObject();

			if(s instanceof RingDebris) {
				Inventory inv = ((RingDebris)s).getInventory();
				if(inv.itemCount() > 0) {
					// TODO: merge logic with ExtractMenuButton
					menu.add(new LootMenuButton("Extract", inv));
				}
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Extract natural resources from planets and asteroids.");
	}
}
