package vekta.economy;

public class TemporaryModifier implements ProductivityModifier {
	private final String name;
	private float amount;
	private float decay;

	public TemporaryModifier(String name, float amount, float decay) {
		this.name = name;
		this.amount = amount;
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

	public void setDecay(float decay) {
		this.decay = decay;
	}

	@Override
	public float updateModifier(Economy economy) {
		float sign = Math.signum(amount);
		float abs = amount * sign - decay;
		if(abs <= 0) {
			economy.removeModifier(this);
			return 0;
		}
		
		amount = abs * sign;
		return amount;
	}
}
