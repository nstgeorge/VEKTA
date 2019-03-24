package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.item.Inventory;
import vekta.menu.Menu;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

/**
 * Menu renderer for trade interactions
 */
public class InventoryMenuHandle extends SideLayoutMenuHandle {
	private final Inventory inv;

	public InventoryMenuHandle(Inventory inv) {
		super(false);

		this.inv = inv;
	}

	public Inventory getInventory() {
		return inv;
	}

	@Override
	public String getSelectVerb() {
		return "jettison";
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_INVENTORY;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(UI_COLOR);
		v.text("Gold: [" + getInventory().getMoney() + " G]", getButtonX(), getButtonY(-2));
	}
}
