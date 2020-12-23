package vekta.terrain.condition;

public interface Condition<T> {

	float getScore(T target);

	default boolean isBetween(T target, float min, float max) {
		float amount = getScore(target);
		return amount >= min && amount <= max;
	}
}
