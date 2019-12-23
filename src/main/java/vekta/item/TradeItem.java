package vekta.item;

import vekta.InfoGroup;
import vekta.Player;
import vekta.Resources;

public class TradeItem extends BasicItem {
	private String description;

	public TradeItem(String name, ItemType type) {
		super(name, type);
	}

	public TradeItem(String name, String description, ItemType type) {
		this(name, type);

		this.description = description;
	}

	public String getDescription() {
		if(description == null) {
			description = Resources.generateString("description_item_" + (getType() == ItemType.COMMON ? "common" : "rare"))
					.replaceAll("\\*", getName());
		}
		return description;
	}

	@Override
	public void onInfo(InfoGroup info) {
		String desc = getDescription();
		if(desc != null) {
			info.addDescription(desc);
		}
	}

	@Override
	public void onAdd(Player player) {
		super.onAdd(player);

		// Remove description on purchase/loot
		description = null;
	}
}
