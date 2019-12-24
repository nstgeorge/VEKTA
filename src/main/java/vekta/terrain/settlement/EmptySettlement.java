package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Player;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuButton;
import vekta.spawner.ItemGenerator;

public class EmptySettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public EmptySettlement(Faction faction) {
		super(faction, "empty");

		ItemGenerator.addLoot(getInventory(), 1);
	}

	@Override
	public String getGenericName() {
		return "Territory";
	}

	@Override
	public float getValueScale() {
		return .5F;
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
			menu.add(new LootMenuButton("Scavenge", menu.getPlayer().getInventory(), getInventory()));
		}
	}
}

