package vekta.terrain.settlement;

import vekta.spawner.PersonGenerator;
import vekta.Resources;
import vekta.terrain.Terrain;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.building.MarketBuilding;

import static vekta.Vekta.v;

public class RuralSettlement extends Settlement {

	public RuralSettlement() {
		add(new MarketBuilding(2));
		
		int personCt = (int)v.random(1, 3);
		for(int i = 0; i < personCt; i++) {
			add(new HouseBuilding(PersonGenerator.createPerson()));
		}
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
		return Resources.generateString("overview_rural");
	}
}
