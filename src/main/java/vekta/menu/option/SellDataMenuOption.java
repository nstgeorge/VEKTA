package vekta.menu.option;

import vekta.item.Inventory;
import vekta.knowledge.Knowledge;
import vekta.menu.Menu;
import vekta.menu.handle.TradeMenuHandle;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.moneyString;
import static vekta.Vekta.setContext;

public class SellDataMenuOption implements MenuOption {
	private final Settlement settlement;
	private final Inventory inventory;

	public SellDataMenuOption(Settlement settlement, Inventory inventory) {
		this.settlement = settlement;
		this.inventory = inventory;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getName() {
		return "Sell Data";
	}

	@Override
	public void onSelect(Menu menu) {
		menu.getPlayer().cleanupKnowledge();

		Menu sub = new Menu(menu, new TradeMenuHandle(false, getInventory()));
		for(Knowledge k : menu.getPlayer().getKnowledgePrices().keySet()) {
			// TODO: convert to DataTradeOption
			sub.add(new CustomOption(moneyString(k.getName() + " Data", k.getArchiveValue()), m -> {
				int price = m.getPlayer().getKnowledgePrices().get(k);
				if(getInventory().remove(price)) {
					m.getPlayer().getInventory().add(price);
					m.getPlayer().getKnowledgePrices().remove(k);
				}
			}).withColor(k.getColor(menu.getPlayer())).withRemoval());
		}
		sub.addDefault();
		setContext(sub);
	}
}
