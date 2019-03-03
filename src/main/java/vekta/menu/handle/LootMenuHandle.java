package vekta.menu.handle;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.UI_COLOR;

/**
 * Menu renderer for scavenging
 */
public class LootMenuHandle extends MenuHandle {
	private final Inventory inv;

	public LootMenuHandle(MenuOption defaultOption, Inventory inv) {
		super(defaultOption);

		this.inv = inv;
	}

	@Override
	public int getSpacing() {
		return 70;
	}

	@Override
	public int getButtonWidth() {
		return v.width * 2 / 3;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i) - 64;
	}

	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(UI_COLOR);
		v.text("Items Found: " + inv.size(), v.width / 2F, v.height / 4F);
	}
}
