package vekta.terrain.settlement;

import vekta.Faction;
import vekta.spawner.PersonGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.CapitalBuilding;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.building.RefineryBuilding;

import static vekta.Vekta.v;

public class CitySettlement extends Settlement {

	public CitySettlement(Faction faction) {
		super(faction, "city");

		add(new CapitalBuilding(this));

		if(v.chance(.75F)) {
			District district = new District("Trade District");
			for(MarketBuilding building : WorldGenerator.randomMarkets(3, .5F)) {
				district.add(building);
			}
			add(district);
		}

		if(v.chance(.75F)) {
			District district = new District("Industrial District");
			district.add(new RefineryBuilding());
			add(district);
		}
	}

	@Override
	public String getTypeString() {
		return "City";
	}

	@Override
	public void onSetup() {
		getTerrain().addFeature("Urban");

		// Add extra people
		int personCt = (int)v.random(4) + 1;
		for(int i = 0; i < personCt; i++) {
			PersonGenerator.createPerson(this);
		}
	}

	@Override
	public float chooseStartingValue() {
		return v.random(5, 10);
	}

	@Override
	public float getEconomicInfluence() {
		return 1;
	}
}
