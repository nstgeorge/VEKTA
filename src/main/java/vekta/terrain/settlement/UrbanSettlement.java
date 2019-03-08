package vekta.terrain.settlement;

import vekta.spawner.PersonGenerator;
import vekta.Resources;
import vekta.terrain.Terrain;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.building.MarketBuilding;

import static vekta.Vekta.v;

public class UrbanSettlement extends Settlement {

	public UrbanSettlement() {
		// TODO: add District settlement parts
		add(new MarketBuilding(3));

		int personCt = (int)v.random(1, 5);
		for(int i = 0; i < personCt; i++) {
			add(new HouseBuilding(PersonGenerator.createPerson()));
		}
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
