package vekta.overlay.indicator;

import java.util.function.Supplier;

/**
 * Represents a value graphically to the user.
 * The value function is called on every draw.
 *
 * @param <T> Type that the value function returns
 */
public abstract class Indicator<T> {
	private final String name;
	private final Supplier<T> value;

	private final float x;
	private final float y;
	private int color;

	public Indicator(String name, Supplier<T> value, float x, float y, int color) {
		this.name = name;
		this.value = value;
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	// Protected because this should probably be split into subclass-specific accessors
	protected T getValue() {
		return value.get();
	}

	public int getColor() {
		return color;
	}

	public void setColor(int c) {
		color = c;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public abstract void draw();
}
