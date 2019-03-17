package vekta.spawner.economy;

import vekta.economy.BubbleModifier;
import vekta.economy.Economy;
import vekta.spawner.EconomyGenerator;
import vekta.spawner.ItemGenerator;

import static vekta.Vekta.v;

public class BubbleSpawner implements EconomyGenerator.EconomySpawner {
	private static final float BURST_CHANCE_SCALE = .5F;

	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public void spawn(Economy economy) {
		float amount = v.random(5, 10);
		economy.addModifier(new BubbleModifier(ItemGenerator.randomCategory(), amount, BURST_CHANCE_SCALE / amount));
	}
}
