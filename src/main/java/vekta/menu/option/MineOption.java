package vekta.menu.option;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.terrain.Terrain;

public class MineOption implements MenuOption {
	private final Terrain terrain;
	private final Inventory inv;
	private final int amount;

	public MineOption(Terrain terrain, Inventory inv, int amount) {
		this.terrain = terrain;
		this.inv = inv;
		this.amount = amount;
	}

	@Override
	public String getName() {
		return "Mine Resources [+" + amount + " G]";
	}

	@Override
	public void select(Menu menu) {
		inv.add(amount);
		menu.remove(this);
	}
}
