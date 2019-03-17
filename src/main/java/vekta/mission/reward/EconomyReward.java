package vekta.mission.reward;

import vekta.Player;
import vekta.economy.Economy;
import vekta.economy.EconomyDescriptor;
import vekta.economy.ProductivityModifier;
import vekta.mission.Mission;

public class EconomyReward extends Reward {
	private final EconomyDescriptor descriptor;
	private final ProductivityModifier modifier;

	public EconomyReward(EconomyDescriptor descriptor, ProductivityModifier modifier) {
		this.descriptor = descriptor;
		this.modifier = modifier;
	}

	@Override
	public String getName() {
		return modifier.getModifierName() + " (" + descriptor.getName() + ")";
	}

	public Economy getEconomy() {
		return descriptor.getEconomy();
	}

	public ProductivityModifier getModifier() {
		return modifier;
	}

	@Override
	public int getColor() {
		return descriptor.getColor();
	}

	@Override
	public final void onReward(Mission mission, Player player) {
		getEconomy().addModifier(getModifier());
	}
}
