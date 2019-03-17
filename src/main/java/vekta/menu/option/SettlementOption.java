package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.SecurityMenuHandle;
import vekta.menu.handle.SettlementMenuHandle;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.setContext;

public class SettlementOption implements MenuOption {
	private final Settlement settlement;

	public SettlementOption(Settlement settlement) {
		this.settlement = settlement;
	}

	@Override
	public String getName() {
		return "Visit " + settlement.getGenericName();
	}

	//	@Override
	//	public int getColor() {
	//		return settlement.getFaction().getColor();
	//	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new SettlementMenuHandle(new BackOption(menu), getSettlement()));
		getSettlement().setupMenu(sub);
		sub.addDefault();

		if(getSettlement().getFaction().isEnemy(menu.getPlayer().getFaction())) {
			Menu security = new Menu(menu.getPlayer(), new SecurityMenuHandle(sub.getDefault(), sub, getSettlement().getFaction()));
			security.addDefault();
			setContext(security);
		}
		else {
			setContext(sub);
		}
	}
}
