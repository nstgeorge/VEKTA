package vekta.menu.option;

import vekta.item.BlueprintItem;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.SideLayoutMenuHandle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static vekta.Vekta.setContext;

public class CraftMenuButton implements ButtonOption {

	private final String name;

	public CraftMenuButton(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onSelect(Menu menu) {
		Inventory inv = menu.getPlayer().getInventory();
		List<BlueprintItem> blueprints = new ArrayList<>();
		for(Item item : inv) {
			if(item instanceof BlueprintItem) {
				blueprints.add((BlueprintItem)item);
			}
		}
		blueprints.sort(Comparator.comparing(BlueprintItem::getResultName));

		Menu sub = new Menu(menu, new SideLayoutMenuHandle(true));
		for(BlueprintItem blueprint : blueprints) {
			sub.add(new CraftButton(inv, blueprint));
		}
		sub.addDefault();
		setContext(sub);

		menu.remove(this);
	}
}
