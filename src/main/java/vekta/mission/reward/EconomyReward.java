package vekta.mission.reward;

import vekta.player.Player;
import vekta.economy.Economy;
import vekta.economy.ProductivityModifier;
import vekta.mission.Mission;

public class EconomyReward extends Reward {
	private final Economy economy;
	private final ProductivityModifier modifier;

	public EconomyReward(Economy economy, ProductivityModifier modifier) {
		this.economy = economy;
		this.modifier = modifier;
	}

	@Override
	public String getName() {
		return modifier.getModifierName() + " (" + economy.getContainer().getName() + ")";
	}

	public Economy getEconomy() {
		return economy;
	}

	public ProductivityModifier getModifier() {
		return modifier;
	}

	@Override
	public int getColor() {
		return economy.getContainer().getColor();
	}

	@Override
	public String getDisplayText() {
		return "Result: " + getName();
	}

	@Override
	public final void onReward(Mission mission, Player player) {
		getEconomy().addModifier(getModifier());
	}
}
