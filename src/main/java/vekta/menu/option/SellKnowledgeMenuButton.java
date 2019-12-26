package vekta.menu.option;

import vekta.item.Inventory;
import vekta.knowledge.Knowledge;
import vekta.menu.Menu;
import vekta.menu.handle.TradeMenuHandle;
import vekta.terrain.settlement.Settlement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static vekta.Vekta.setContext;

public class SellKnowledgeMenuButton implements ButtonOption {
	private final Settlement settlement;
	private final Inventory inventory;

	public SellKnowledgeMenuButton(Settlement settlement, Inventory inventory) {
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
		List<Knowledge> knowledge = new ArrayList<>(menu.getPlayer().getKnowledgePrices().keySet());
		knowledge.sort(Comparator.comparingInt(k -> -k.getArchiveValue()));
		for(Knowledge k : knowledge) {
			sub.add(new SellDataButton(getInventory(), menu.getPlayer(), k));
		}
		sub.addDefault();
		setContext(sub);
	}
}
