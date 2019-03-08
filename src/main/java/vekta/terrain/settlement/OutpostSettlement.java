package vekta.terrain.settlement;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.v;

public class OutpostSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public OutpostSettlement() {
		super("outpost");

		if(v.chance(.5F)) {
			add(WorldGenerator.randomMarket(1));
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
