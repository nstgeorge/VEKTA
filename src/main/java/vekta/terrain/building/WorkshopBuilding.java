package vekta.terrain.building;

import vekta.menu.Menu;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

public class WorkshopBuilding implements SettlementPart {
	public WorkshopBuilding() {
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Workshop";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.INDUSTRIAL;
	}

	@Override
	public void setup(LandingSite site) {
		site.getTerrain().addFeature("Industrial");
	}

	@Override
	public void setupMenu(Menu menu) {
		// TODO implement
	}
}
