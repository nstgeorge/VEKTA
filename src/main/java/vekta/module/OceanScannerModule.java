package vekta.module;

import java.util.List;

import vekta.knowledge.OceanKnowledge;
import vekta.menu.Menu;
import vekta.menu.handle.LocationMenuHandle;
import vekta.menu.handle.OceanMenuHandle;
import vekta.menu.option.LootMenuButton;
import vekta.menu.option.ScanOceanButton;
import vekta.terrain.location.Location;
import vekta.terrain.location.OceanLocation;
import vekta.util.InfoGroup;

public class OceanScannerModule extends ShipModule {
	private float strength;

	public OceanScannerModule() {
		this(1);
	}

	public OceanScannerModule(float strength) {
		this.strength = strength;
	}

	public float getStrength() {
		return strength;
	}

	@Override
	public String getName() {
		return "Oceanic Scanner v" + getStrength();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.OCEAN;
	}

	@Override
	public int getMass() {
		return 1000;
	}

	@Override
	public float getValueScale() {
		return 1.5F * getStrength();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof OceanScannerModule && getStrength() > ((OceanScannerModule) other).getStrength();
	}

	@Override
	public BaseModule createVariant() {
		return new OceanScannerModule(chooseInclusive(.1F, 2, .1F));
	}

	@Override
	public void onMenu(Menu menu) {
		if (menu.getHandle() instanceof OceanMenuHandle) {
			Location location = ((LocationMenuHandle) menu.getHandle()).getLocation();

			if (location instanceof OceanLocation) {
				OceanLocation ocean = (OceanLocation) location;

				List<OceanKnowledge> knowledgeList = menu.getPlayer().findKnowledge(OceanKnowledge.class,
						o -> o.getSpaceObject() == ocean.getPlanet());
				if (knowledgeList.isEmpty()) {
					menu.add(new ScanOceanButton((OceanLocation) location, getStrength()));
				} else {
					for (OceanKnowledge knowledge : knowledgeList) {
						if (knowledge.getInventory().itemCount() > 0) {
							menu.add(new LootMenuButton("Salvage", knowledge.getInventory()));
						}
					}
				}
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Detect underwater features on oceanic planets.");
	}
}
