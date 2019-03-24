package vekta.menu.handle;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeOption;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

/**
 * Menu renderer for trade interactions
 */
public class TradeMenuHandle extends SideLayoutMenuHandle {
	private final Inventory to;

	public TradeMenuHandle(boolean buying, Inventory to) {
		super(buying);

		this.to = to;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		boolean buying = isLeftSide();

		v.textSize(32);
		v.fill(buying ? UI_COLOR : 100);
		v.text((buying ? "You" : "They") + " have: [" + to.getMoney() + " G]", getButtonX(), getButtonY(-2));
	}

	@Override
	protected void drawButton(Menu menu, MenuOption opt, int index) {
		super.drawButton(menu, opt, index);

		if(!isLeftSide() && opt instanceof ItemTradeOption) {
			ItemTradeOption trade = (ItemTradeOption)opt;

			int profit = trade.getProfit(menu.getPlayer());

			v.fill(profit > 0 ? UI_COLOR : 100);
			v.text((profit > 0 ? "+" : "") + profit, getButtonX() + getButtonWidth() / 2F + 50, getButtonY(index));
		}
	}
}
