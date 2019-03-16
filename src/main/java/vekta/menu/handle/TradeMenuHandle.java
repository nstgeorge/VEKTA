package vekta.menu.handle;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeOption;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

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
		return super.getButtonWidth() * 2;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(buying ? UI_COLOR : 100);
		v.text((buying ? "You" : "They") + " have: [" + to.getMoney() + " G]", getButtonX(), getButtonY(-2));
	}

	@Override
	void drawButton(Menu menu, MenuOption opt, int index) {
		super.drawButton(menu, opt, index);

		if(!buying && opt instanceof ItemTradeOption) {
			ItemTradeOption trade = (ItemTradeOption)opt;
			
			int profit = trade.getProfit(menu.getPlayer());

			v.fill(profit > 0 ? UI_COLOR : 100);
			v.text((profit > 0 ? "+" : "") + profit, getButtonX() + getButtonWidth() * 3 / 4F, getButtonY(index));
		}
	}
}
