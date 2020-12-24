package vekta.mission.reward;

import vekta.mission.Mission;
import vekta.player.Player;

import static vekta.Vekta.v;

public class MoneyReward extends Reward {
	private final int amount;

	public MoneyReward(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public String getName() {
		return "+ " + getAmount() + " G";
	}

	@Override
	public int getColor() {
		return v.color(255);
	}

	@Override
	public void onReward(Mission mission, Player player) {
		player.getInventory().add(getAmount());
	}
}
