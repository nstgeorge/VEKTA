package vekta.terrain.settlement;

import vekta.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.v;

public class OutpostSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public OutpostSettlement(Faction faction) {
		super(faction, "outpost");

		getEconomy().setValue(v.random(.01F, .1F));

		//		addPopulation((int)v.random(5) + 1);

		if(v.chance(.5F)) {
			add(WorldGenerator.createMarket(1));
		}
	}

	@Override
	public String getTypeString() {
		return "Outpost";
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void onSettlementMenu(Menu menu) {
	}
}
