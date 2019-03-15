package vekta.terrain.settlement;

import vekta.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuOption;
import vekta.spawner.ItemGenerator;

public class EmptySettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public EmptySettlement(Faction faction) {
		super(faction, "empty");

		ItemGenerator.addLoot(getInventory(), 1);
	}

	@Override
	public String getTypeString() {
		return "Territory";
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

	@Override
	public float chooseStartingValue() {
		return 0;
	}

	@Override
	public float getEconomicInfluence() {
		return 0;
	}
}
