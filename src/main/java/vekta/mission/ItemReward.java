package vekta.mission;

import vekta.Player;
import vekta.item.Item;

public class ItemReward extends Reward {
	private final Item item;

	public ItemReward(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String getName() {
		return getItem().getName();
	}

	@Override
	public int getColor() {
		return getItem().getType().getColor();
	}

	@Override
	public void onReward(Mission mission, Player player) {
		player.getShip().getInventory().add(getItem());
	}
}
