package vekta.terrain.settlement;

import vekta.Faction;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.RefineryBuilding;

import static vekta.Vekta.*;

public class CitySettlement extends Settlement {

	public CitySettlement(Faction faction) {
		super(faction, "city");

		// TODO: add District settlement parts
		add(WorldGenerator.randomMarket(3));
		
		if(v.chance(.8F)) {
			add(new RefineryBuilding());
		}
	}

	@Override
	public String getTypeString() {
		return "City";
	}

	@Override
	public void onSetup() {
		getTerrain().addFeature("Urban");
	}

}
