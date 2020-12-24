package vekta.menu.handle;

import vekta.item.Inventory;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

/**
 * Menu renderer for looting/scavenging
 */
public class LootMenuHandle extends MenuHandle {
	private final Inventory inv;

	public LootMenuHandle(Inventory inv) {
		this.inv = inv;
	}

	public Inventory getInventory() {
		return inv;
	}

	@Override
	public int getSpacing() {
		return 70;
	}

	@Override
	public int getItemWidth() {
		return v.width * 2 / 3;
	}

	@Override
	public void render() {
		super.render();

		v.textSize(32);
		v.fill(UI_COLOR);
		v.text("Items Found: " + inv.itemCount(), getItemX(), getItemY(-2));
	}
}
