package vekta.terrain.settlement;

import vekta.ItemGenerator;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuOption;

public class AbandonedSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public AbandonedSettlement() {
		ItemGenerator.addLoot(getInventory(), 1);
	}

	@Override
	public String getName() {
		return "Abandoned Settlement";
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String createOverview() {
		return "You find the crumbling remains of an abandoned civilization.";
	}

	@Override
	public void onLandingMenu(Menu menu) {
		menu.add(new LootMenuOption("Scavenge", menu.getPlayer().getInventory(), getInventory()));
	}
}
