package vekta.menu.handle;

import vekta.item.Inventory;
import vekta.Vekta;
import vekta.menu.Menu;
import vekta.menu.MenuOption;
import vekta.menu.TradeOption;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.getInstance;

/**
 * Menu renderer for trading
 */
public class TradeMenuHandle extends MenuHandle {
	private final Inventory you, them;

	public TradeMenuHandle(MenuOption defaultOption, Inventory you, Inventory them) {
		super(defaultOption, 70, Vekta.getInstance().width * 2 / 3);

		this.you = you;
		this.them = them;
	}

	public void render(Menu menu) {
		super.render(menu);

		Vekta v = getInstance();
		v.textSize(32);
		v.fill(UI_COLOR);
		v.text("You have: [" + you.getMoney() + " G]", v.width / 2F, v.height / 4F);
		v.fill(100);
		v.text("They have: [" + them.getMoney() + " G]", v.width / 2F, v.height / 4F + 48);
	}

	@Override
	int getButtonColor(MenuOption opt) {
		if(opt instanceof TradeOption) {
			return ((TradeOption)opt).getItem().getType().getColor();
		}
		return super.getButtonColor(opt);
	}
}
