package vekta.terrain.settlement;

import vekta.faction.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.spawner.PersonGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.*;

import static vekta.Vekta.v;

public class CitySettlement extends Settlement {

	public CitySettlement(Faction faction) {
		super(faction, "city");

		add(new District(this, "Trade District", BuildingType.MARKET));
		add(new District(this, "Industrial District", BuildingType.INDUSTRIAL));
		add(new District(this, "Housing District", BuildingType.RESIDENTIAL));
		add(new District(this, "Financial District", BuildingType.ECONOMY));
		//		add(new District(this, "Government Offices", BuildingType.GOVERNMENT));

		add(new CapitalBuilding("Governor", this));
		add(new AcademyBuilding(this));

		for(MarketBuilding building : WorldGenerator.randomMarkets(3, .4F)) {
			add(building);
		}

		if(v.chance(.75F)) {
			if(v.chance(.75F)) {
				add(new JunkyardBuilding());
			}
			add(new RefineryBuilding());
			add(new WorkshopBuilding());
		}

		add(new ForumBuilding(this, (int)(v.random(10, 20))));
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
	public void setupEconomy(Economy economy) {
		economy.setValue(v.random(5, 10));
		economy.addModifier(new NoiseModifier(2));
	}
}
