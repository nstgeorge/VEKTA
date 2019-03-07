package vekta.terrain.settlement;

import vekta.Resources;
import vekta.terrain.Terrain;
import vekta.terrain.building.MarketBuilding;

public class UrbanSettlement extends Settlement {

	public UrbanSettlement() {
		// TODO: add District settlement parts
		add(new MarketBuilding(3));
	}

	@Override
	public String getName() {
		return "Urban Settlement";
	}

	@Override
	public void onTerrain(Terrain terrain) {
		terrain.addFeature("Urban");
	}

	@Override
	public String createOverview() {
		return Resources.generateString("overview_urban");
	}
}
