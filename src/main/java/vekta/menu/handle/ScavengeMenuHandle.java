package vekta.menu.handle;

import vekta.Vekta;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.getInstance;

/**
 * Menu renderer for scavenging
 */
public class ScavengeMenuHandle extends MenuHandle {
	private final Inventory inv;

	public ScavengeMenuHandle(MenuOption defaultOption, Inventory inv) {
		super(defaultOption, 70, Vekta.getInstance().width * 2 / 3);

		this.inv = inv;
	}

	public void render(Menu menu) {
		super.render(menu);

		Vekta v = getInstance();
		v.textSize(32);
		v.fill(UI_COLOR);
		v.text("Items Found: " + inv.size(), v.width / 2F, v.height / 4F);
	}
}
