package vekta.spawner;

public class WeightedOption<T> implements Weighted {
	private final float weight;
	private final T value;

	public WeightedOption(float weight, T value) {
		this.weight = weight;
		this.value = value;
	}

	@Override
	public float getWeight() {
		return weight;
	}

	public T getValue() {
		return value;
	}
}
