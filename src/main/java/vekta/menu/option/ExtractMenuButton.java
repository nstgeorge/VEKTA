package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LootMenuHandle;
import vekta.terrain.location.MiningLocation;

import static vekta.Vekta.setContext;

public class ExtractMenuButton extends ButtonOption {
	private final MiningLocation location;
	private final float efficiency;

	public ExtractMenuButton(MiningLocation location, float efficiency) {
		this.location = location;
		this.efficiency = efficiency;
	}

	@Override
	public String getName() {
		return "Extract";
	}

	@Override
	public void onSelect(Menu menu) {

		Inventory loot = location.extract(efficiency);

		Menu sub = new Menu(menu, new LootMenuHandle(loot));
		for(Item item : loot) {
			sub.add(new ItemTradeButton(true, menu.getPlayer(), loot, item));
		}
		sub.addDefault();
		setContext(sub);

		//		location.getTerrain().removeFeature("Mineable");
		menu.remove(this);
	}
}
