package vekta.terrain.settlement;

import vekta.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.menu.Menu;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.v;

public class OutpostSettlement extends Settlement {
	
	public OutpostSettlement(Faction faction) {
		super(faction, "outpost");

		//		if(v.chance(.5F)) {
		add(WorldGenerator.createMarket(1));
		//		}
	}
	
	@Override
	public String getGenericName() {
		return "Outpost";
	}

	@Override
	public void onSetupEconomy(Economy economy) {
		economy.setValue(v.random(.2F, 1));
		economy.addModifier(new NoiseModifier(.1F));
	}

	@Override
	public void onSettlementMenu(Menu menu) {
	}
}
