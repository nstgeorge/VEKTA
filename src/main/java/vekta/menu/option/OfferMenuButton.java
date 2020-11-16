package vekta.menu.option;

import vekta.deity.Deity;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.category.ItemCategory;
import vekta.item.category.ItemTypeCategory;
import vekta.menu.Menu;
import vekta.menu.handle.OfferingMenuHandle;

import static vekta.Vekta.setContext;

public class OfferMenuButton extends ButtonOption {
	private final Deity deity;

	public OfferMenuButton(Deity deity) {
		this.deity = deity;
	}

	public Deity getDeity() {
		return deity;
	}

	@Override
	public String getName() {
		return "Make Offering (" + getDeity().getOfferCategory().getName() + ")";
	}

	public ItemCategory getCategory() {
		return getDeity().getOfferCategory();
	}

	@Override
	public int getColor() {
		ItemCategory category = getCategory();
		return category instanceof ItemTypeCategory ? ((ItemTypeCategory)category).getType().getColor() : super.getColor();
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new OfferingMenuHandle(getDeity()));
		Inventory inv = menu.getPlayer().getInventory();
		for(Item item : inv) {
			if(getCategory().isIncluded(item)) {
				sub.add(new OfferButton(item, inv, getDeity()));
			}
		}
		sub.addDefault();
		setContext(sub);
	}
}
