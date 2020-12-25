package vekta.terrain.settlement;

import vekta.Resources;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.faction.Faction;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.PersonGenerator;
import vekta.spawner.SettlementGenerator;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.building.*;

import java.util.List;
import java.util.Set;

import static vekta.Vekta.v;

public class TownSettlement extends Settlement {

	public TownSettlement(TerrestrialPlanet planet, Faction faction) {
		super(planet, faction, "town");

		add(new District(this, "Marketplace", BuildingType.MARKET));
		if(v.chance(.75F)) {
			add(new District(this, Resources.generateString("town_social"), BuildingType.RESIDENTIAL));
		}

		add(new CapitalBuilding("Mayor", this));

		add(new ForumBuilding(this, (int)(v.random(5, 10))));

		List<MarketBuilding> buildings = SettlementGenerator.randomMarkets(2, .1F);
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
			add(SettlementGenerator.createMarket(2));
		}

		if(v.chance(.2F)) {
			add(new RefineryBuilding());
		}

		// Add extra people
		int personCt = (int)v.random(2) + 1;
		for(int i = 0; i < personCt; i++) {
			PersonGenerator.createPerson(this);
		}
	}

	@Override
	public String getGenericName() {
		return "Town";
	}

	@Override
	public void onSettlementSurveyTags(Set<String> tags) {
		tags.add("Rural");
	}

	@Override
	public void initEconomy(Economy economy) {
		economy.setValue(v.random(2, 5));
		economy.addModifier(new NoiseModifier(1));
	}
}
