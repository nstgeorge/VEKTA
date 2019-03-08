package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;

/**
 * Menu renderer for landing on planets
 */
public class SettlementMenuHandle extends MenuHandle {
	private final Settlement settlement;

	public SettlementMenuHandle(MenuOption def, Settlement settlement) {
		super(def);

		this.settlement = settlement;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public int getButtonWidth() {
		return v.width * 2 / 3;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i - 1);
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2F, getButtonY(-2));
		v.textSize(48);
		//		v.fill(settlement.getColor());
		v.fill(200);
		v.text(settlement.getName(), v.width / 2F, getButtonY(-1));
		//		v.textSize(20);
		//		v.fill(100);
		//		v.text(getSettlement().getOverview(), v.width / 2F, v.height / 4F + 100);
	}
}
