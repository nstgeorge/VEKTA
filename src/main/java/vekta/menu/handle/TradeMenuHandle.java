package vekta.menu.handle;

import vekta.Vekta;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.getInstance;

/**
 * Menu renderer for trading
 */
public class TradeMenuHandle extends MenuHandle {
	private boolean buying;
	private final Inventory to;

	public TradeMenuHandle(MenuOption defaultOption, boolean buying, Inventory to) {
		super(defaultOption);

		this.buying = buying;
		this.to = to;
	}

	@Override
	public int getSpacing() {
		return 70;
	}

	@Override
	public int getButtonWidth() {
		return v.width * 2 / 3;
	}

	public void render(Menu menu) {
		super.render(menu);

		Vekta v = getInstance();
		v.textSize(32);
		v.fill(buying ? UI_COLOR : 100);
		v.text((buying ? "You" : "They") + " have: [" + to.getMoney() + " G]", v.width / 2F, v.height / 4F + 48);
	}
}
