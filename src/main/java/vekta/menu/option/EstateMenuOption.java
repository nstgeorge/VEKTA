package vekta.menu.option;

import vekta.item.EconomyItem;
import vekta.menu.Menu;
import vekta.menu.handle.EconomyMenuHandle;

import java.util.List;

import static vekta.Vekta.setContext;

public class EstateMenuOption implements MenuOption {
	private final List<EconomyItem> items;

	public EstateMenuOption(List<EconomyItem> items) {
		this.items = items;
	}

	@Override
	public String getName() {
		return "Real Estate";
	}

	public List<EconomyItem> getItems() {
		return items;
	}

	@Override
	public void select(Menu menu) {
		EconomyMenuHandle handle = new EconomyMenuHandle(new BackOption(menu), menu.getPlayer().getInventory(), this::update);
		Menu sub = new Menu(menu.getPlayer(), handle);
		update(sub, handle.isBuying());
		setContext(sub);
	}

	private void update(Menu sub, boolean buying) {
		sub.clear();
		for(EconomyItem item : getItems()) {
			sub.add(new EconomyItemOption(
					sub.getPlayer().getInventory(),
					item,
					0,
					buying,
					this::update));
		}
		sub.addDefault();
	}
}
