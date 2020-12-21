package vekta.menu.handle;

import vekta.item.Inventory;
import vekta.menu.Menu;

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
	public void render() {
		super.render();

		boolean buying = isLeftSide();

		v.textSize(32);
		v.fill(buying ? UI_COLOR : 100);
		v.text((buying ? "You" : "They") + " have: [" + to.getMoney() + " G]", getItemX(), getItemY(-2));
	}
}
