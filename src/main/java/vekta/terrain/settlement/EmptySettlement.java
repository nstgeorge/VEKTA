package vekta.terrain.settlement;

import vekta.faction.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuButton;
import vekta.player.Player;
import vekta.spawner.ItemGenerator;
import vekta.terrain.location.Location;

public class EmptySettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public EmptySettlement(Location location, Faction faction) {
		super(location, faction, "empty");

		ItemGenerator.addLoot(getInventory(), 1);
	}

	@Override
	public String getGenericName() {
		return "Territory";
	}

	@Override
	public float getValueScale() {
		return .1F;
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

