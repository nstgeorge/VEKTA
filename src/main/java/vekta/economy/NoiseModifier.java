package vekta.economy;

import static vekta.Vekta.v;

public class NoiseModifier implements ProductivityModifier {
	private final float randomness;

	public NoiseModifier(float randomness) {
		this.randomness = randomness;
	}
	
	public float getRandomness() {
		return randomness;
	}

	@Override
	public String getModifierName() {
		return "Risk Factors";
	}

	@Override
	public float updateModifier(Economy economy) {
		return v.random(-1, 1) * randomness;
	}
}
