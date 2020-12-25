package vekta.terrain.settlement;

import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.faction.Faction;
import vekta.menu.Menu;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.SettlementGenerator;
import vekta.terrain.location.Location;

import static vekta.Vekta.v;

public class OutpostSettlement extends Settlement {
	
	public OutpostSettlement(TerrestrialPlanet planet, Faction faction) {
		super(planet, faction, "outpost");

		//		if(v.chance(.5F)) {
		add(SettlementGenerator.createMarket(1));
		//		}
	}
	
	@Override
	public String getGenericName() {
		return "Outpost";
	}

	@Override
	public void initEconomy(Economy economy) {
		economy.setValue(v.random(.2F, 1));
		economy.addModifier(new NoiseModifier(.1F));
	}

	@Override
	public void onSettlementMenu(Menu menu) {
	}
}
