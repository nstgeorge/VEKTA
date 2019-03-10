package vekta.terrain.settlement;

import vekta.Faction;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.RefineryBuilding;

import static vekta.Vekta.v;

public class TownSettlement extends Settlement {

	public TownSettlement(Faction faction) {
		super(faction, "town");

		add(WorldGenerator.randomMarket(2));
		
		if(v.chance(.2F)) {
			add(new RefineryBuilding());
		}
	}

	@Override
	public String getTypeString() {
		return "Town";
	}

	@Override
	public void onSetup() {
		getTerrain().addFeature("Rural");
	}
}
