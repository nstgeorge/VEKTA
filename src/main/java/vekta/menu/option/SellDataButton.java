package vekta.menu.option;

import vekta.display.Layout;
import vekta.item.Inventory;
import vekta.knowledge.Knowledge;
import vekta.menu.Menu;
import vekta.player.Player;

import static vekta.Vekta.moneyString;

public class SellDataButton extends ButtonOption implements LayoutAware {
	private final Inventory inventory;
	private final Player player;
	private final Knowledge knowledge;

	private final int price;

	public SellDataButton(Inventory inventory, Player player, Knowledge knowledge) {
		this.inventory = inventory;
		this.player = player;
		this.knowledge = knowledge;

		this.price = player.getKnowledgePrices().get(knowledge);
	}

	@Override
	public String getName() {
		return moneyString(knowledge.getName() + " Data", knowledge.getArchiveValue());
	}

	@Override
	public int getColor() {
		return knowledge.getColor(player);
	}

	@Override
	public boolean isEnabled() {
		return inventory.has(price);
	}

	@Override
	public String getSelectVerb() {
		return "sell";
	}

	@Override
	public void onSelect(Menu menu) {
		if(inventory.remove(price)) {
			player.getInventory().add(price);
			player.getKnowledgePrices().remove(knowledge);
			menu.remove(this);
		}
	}

	@Override
	public void onLayout(Layout layout) {
		knowledge.onLayout(player, layout);
	}
}
