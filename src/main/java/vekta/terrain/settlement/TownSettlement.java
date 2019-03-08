package vekta.terrain.settlement;

import vekta.spawner.WorldGenerator;
import vekta.terrain.Terrain;

public class TownSettlement extends Settlement {

	public TownSettlement() {
		super("town");

		add(WorldGenerator.randomMarket(2));
	}

	@Override
	public String getTypeString() {
		return "Town";
	}

	@Override
	public void onTerrain(Terrain terrain) {
		terrain.addFeature("Rural");
	}
}
