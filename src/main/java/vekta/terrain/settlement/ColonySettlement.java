package vekta.terrain.settlement;

import vekta.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.CapitalBuilding;

public class ColonySettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public ColonySettlement(Faction faction) {
		super(faction, "colony");

		add(new CapitalBuilding(this));
		add(WorldGenerator.randomMarket(1));
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
