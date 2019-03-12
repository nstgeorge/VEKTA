package vekta.terrain.settlement;

import vekta.Faction;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.CapitalBuilding;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.building.RefineryBuilding;

import java.util.List;

import static vekta.Vekta.v;

public class TownSettlement extends Settlement {

	public TownSettlement(Faction faction) {
		super(faction, "town");

		add(new CapitalBuilding(this));

		List<MarketBuilding> buildings = WorldGenerator.randomMarkets(2, .1F);
		District district = new District("Marketplace");
		if(!buildings.isEmpty()) {
			int ct = 0;
			for(MarketBuilding building : buildings) {
				district.add(building);
				// Limit number of marketplaces
				if(++ct == 3) {
					break;
				}
			}
		}
		else {
			// Occasionally have a specialized market
			district.add(WorldGenerator.randomMarket(2));
		}
		add(district);

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
