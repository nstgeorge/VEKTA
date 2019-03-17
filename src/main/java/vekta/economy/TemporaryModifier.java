package vekta.economy;

public class TemporaryModifier implements ProductivityModifier {
	private final String name;
	private float amount;
	private final float decay;

	public TemporaryModifier(String name, float amount, float decay) {
		this.name = name;
		this.amount = amount;
		this.decay = decay;
	}

	@Override
	public String getModifierName() {
		return name;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getDecay() {
		return decay;
	}

	@Override
	public float updateModifier(Economy economy) {
		float sign = Math.signum(amount);
		float abs = amount * sign - decay;
		if(abs <= 0) {
			economy.removeModifier(this);
			return amount = 0;
		}
		return amount = abs * sign;
	}
}
