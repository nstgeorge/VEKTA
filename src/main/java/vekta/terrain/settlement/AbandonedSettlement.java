package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Resources;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuOption;
import vekta.spawner.ItemGenerator;

public class AbandonedSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public AbandonedSettlement(Faction faction, String prevName) {
		super(faction, prevName + " Ruins", Resources.generateString("overview_ruins"));

		ItemGenerator.addLoot(getInventory(), 1);
	}

	@Override
	public String getTypeString() {
		return "Ruins";
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public boolean isInhabited() {
		return false;
	}

	@Override
	public void onSettlementMenu(Menu menu) {
		menu.add(new LootMenuOption("Scavenge", menu.getPlayer().getInventory(), getInventory()));
	}
}
