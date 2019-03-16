package vekta.terrain.settlement;

import vekta.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.v;

public class OutpostSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public OutpostSettlement(Faction faction) {
		super(faction, "outpost");

		//		addPopulation((int)v.random(5) + 1);

		if(v.chance(.5F)) {
			add(WorldGenerator.createMarket(1));
		}
	}

	public Inventory getInventory() {
		return inventory;
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
