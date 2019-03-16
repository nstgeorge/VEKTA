package vekta.terrain.settlement;

import vekta.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.CapitalBuilding;

import static vekta.Vekta.v;

public class ColonySettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public ColonySettlement(Faction faction) {
		super(faction, "colony");

		getEconomy().setValue(v.random(.1F, 1));

		add(new CapitalBuilding(this));
		add(WorldGenerator.createMarket(1));
	}

	@Override
	public String getTypeString() {
		return "Colony";
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void onSettlementMenu(Menu menu) {
	}
}
