package vekta.terrain;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuOption;
import vekta.object.PlayerShip;

public class AbandonedTerrain extends Terrain {
	private final Inventory inventory = new Inventory();
	
	public AbandonedTerrain() {
		if(chance(.5)) {
			add("Habitable");
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getOverview() {
		return "You find the crumbling remains of an abandoned civilization.";
	}

	@Override
	public void setupLandingMenu(PlayerShip ship, Menu menu) {
		menu.add(new LootMenuOption("Scavenge", ship.getInventory(), getInventory()));
	}
}
