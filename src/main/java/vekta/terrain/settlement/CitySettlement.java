package vekta.terrain.settlement;

import vekta.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.spawner.PersonGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.*;

import static vekta.Vekta.v;

public class CitySettlement extends Settlement {

	public CitySettlement(Faction faction) {
		super(faction, "city");

		add(new District("Trade District", BuildingType.MARKET));
		add(new District("Industrial District", BuildingType.INDUSTRIAL));
		add(new District("Housing District", BuildingType.RESIDENTIAL));
		add(new District("Financial District", BuildingType.ECONOMY));
//		add(new District("Government Offices", BuildingType.GOVERNMENT));

		add(new CapitalBuilding(this));

		for(MarketBuilding building : WorldGenerator.randomMarkets(3, .4F)) {
			add(building);
		}

		if(v.chance(.75F)) {
			add(new RefineryBuilding());
		}

		add(new ForumBuilding(this, (int)(v.random(8, 12))));
		add(new ExchangeBuilding());
	}

	@Override
	public String getGenericName() {
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
	public void onSetupEconomy(Economy economy) {
		economy.setValue(v.random(5, 10));
		economy.addModifier(new NoiseModifier(2));
	}
}
