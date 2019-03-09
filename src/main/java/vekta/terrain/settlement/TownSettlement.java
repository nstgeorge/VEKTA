package vekta.terrain.settlement;

import vekta.Faction;
import vekta.spawner.WorldGenerator;

public class TownSettlement extends Settlement {

	public TownSettlement(Faction faction) {
		super(faction, "town");

		add(WorldGenerator.randomMarket(2));
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
