package vekta.item;

import vekta.InfoGroup;
import vekta.Player;
import vekta.Resources;

public class TradeItem extends BasicItem {
	private String description;

	public TradeItem(String name, ItemType type) {
		super(name, type);

		description = Resources.generateString("description_item_" + (getType() == ItemType.COMMON ? "common" : "rare"))
				.replaceAll("\\*", name);
	}

	public String getDescription() {
		return description;
	}

	@Override
	public void onInfo(InfoGroup info) {
		if(getDescription() != null) {
			info.addDescription(getDescription());
		}
	}

	@Override
	public void onAdd(Player player) {
		super.onAdd(player);

		// Remove description on purchase/loot
		description = null;
	}
}
