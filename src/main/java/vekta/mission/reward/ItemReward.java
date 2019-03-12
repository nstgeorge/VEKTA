package vekta.mission.reward;

import vekta.Player;
import vekta.item.Item;
import vekta.mission.Mission;

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
		return getItem().getColor();
	}

	@Override
	public void onReward(Mission mission, Player player) {
		player.getInventory().add(getItem());
	}
}
