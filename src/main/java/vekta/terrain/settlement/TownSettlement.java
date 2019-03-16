package vekta.terrain.settlement;

import vekta.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.spawner.PersonGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.*;

import java.util.List;

import static vekta.Vekta.v;

public class TownSettlement extends Settlement {

	public TownSettlement(Faction faction) {
		super(faction, "town");

		//		addPopulation((int)v.random(5, 100) + 1);

		add(new District("Marketplace", BuildingType.MARKET));

		add(new CapitalBuilding(this));

		add(new EconomyBuilding(this, (int)(v.random(5, 10))));

		List<MarketBuilding> buildings = WorldGenerator.randomMarkets(2, .1F);
		if(!buildings.isEmpty()) {
			int ct = 0;
			for(MarketBuilding building : buildings) {
				add(building);
				// Limit number of marketplaces
				if(++ct == 3) {
					break;
				}
			}
		}
		else {
			// Occasionally have a specialized market
			add(WorldGenerator.createMarket(2));
		}

		if(v.chance(.2F)) {
			add(new RefineryBuilding());
		}
	}

	@Override
	public String getGenericName() {
		return "Town";
	}

	@Override
	public void onSetup() {
		getTerrain().addFeature("Rural");

		// Add extra people
		int personCt = (int)v.random(2) + 1;
		for(int i = 0; i < personCt; i++) {
			PersonGenerator.createPerson(this);
		}
	}

	@Override
	public void onSetupEconomy(Economy economy) {
		economy.setValue(v.random(2, 5));
		economy.addModifier(new NoiseModifier(1));
	}
}
