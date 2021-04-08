package vekta.overlay.indicator;

import java.util.function.Function;

/**
 * Represents a value graphically to the user.
 * The value function is called on every draw.
 *
 * @param <T> Type that the value function requires (Float is used a placeholder if none is required)
 * @param <R> Type that the value function returns
 */
public abstract class Indicator<T, R> {
	String name;
	Function<T, R> value;

	float locX;
	float locY;
	int color;

	public Indicator(String name, Function<T, R> value, float locX, float locY, int color) {
		this.name = name;
		this.value = value;
		this.locX = locX;
		this.locY = locY;
		this.color = color;
	}

	String getName() {
		return name;
	}

	public R getValue(T arg) {
		return value.apply(arg);
	}

	public R getValue() {
		return getValue(null);
	}

	public void setValue(Function<T, R> value) {
		this.value = value;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int c) {
		color = c;
	}

	public float getLocX() {
		return locX;
	}

	public float getLocY() {
		return locY;
	}

	public abstract void draw();
}
