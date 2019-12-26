package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Player;
import vekta.Resources;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuButton;
import vekta.spawner.ItemGenerator;

public class AbandonedSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public AbandonedSettlement(Faction faction, String prevName) {
		super(faction, prevName + " Ruins", Resources.generateString("overview_ruins"));

		ItemGenerator.addLoot(getInventory(), 2);
	}

	@Override
	public String getGenericName() {
		return "Ruins";
	}

	@Override
	public float getValueScale() {
		return 2;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public boolean hasSecurity(Player player) {
		return false;
	}

	@Override
	public void onSettlementMenu(Menu menu) {
		if(getInventory().itemCount() > 0) {
			menu.add(new LootMenuButton("Scavenge", getInventory()));
		}
	}
}
