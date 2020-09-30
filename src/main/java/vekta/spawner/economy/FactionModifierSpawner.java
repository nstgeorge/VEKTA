package vekta.spawner.economy;

import vekta.Resources;
import vekta.economy.Economy;
import vekta.economy.TemporaryModifier;
import vekta.spawner.EconomyGenerator;

import static processing.core.PApplet.sqrt;
import static vekta.Vekta.v;

public class FactionModifierSpawner implements EconomyGenerator.EconomySpawner {
	private static final float DECAY_RATE = .2F;

	@Override
	public float getWeight() {
		return 3;
	}

	@Override
	public void spawn(Economy economy) {
		String[] args = Resources.generateString("modifier_faction").split(":", 2);
		String name = args[0].trim();
		float amount = args.length > 1
				? Float.parseFloat(args[1].trim()) * v.random(.5F, 1)
				: v.random(-1, 1);

		float scale = v.random(sqrt(economy.getValue()));
		economy.addModifier(new TemporaryModifier(name, amount * scale, v.random(scale * DECAY_RATE)));
	}
}
