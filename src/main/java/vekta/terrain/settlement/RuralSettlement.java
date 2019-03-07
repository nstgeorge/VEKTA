package vekta.terrain.settlement;

import vekta.Resources;
import vekta.terrain.Terrain;
import vekta.terrain.building.MarketBuilding;

import static vekta.Vekta.v;

public class RuralSettlement extends Settlement {

	public RuralSettlement() {
		add(new MarketBuilding(2));
	}

	@Override
	public String getName() {
		return "Rural Settlement";
	}

	@Override
	public void onTerrain(Terrain terrain) {
		terrain.addFeature("Rural");
	}

	@Override
	public String createOverview() {
		return v.random(Resources.getStrings("overview_rural"));
	}
}
