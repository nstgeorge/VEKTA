package vekta.overlay.indicator;

import java.io.Serializable;
import java.util.function.Supplier;

import static processing.core.PConstants.*;
import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

/**
 * A collection of meter indicators, best used to show a float that should be contained within provided bounds.
 * If the value leaves the bounds, the meter flashes red.
 */
public class MeterIndicator extends Indicator<Float> implements Serializable {

	private final IndicatorType type;
	private final float min;
	private float max;
	private final float size;
	private final float thickness;

	public MeterIndicator(String name, IndicatorType type, DynamicValue<Float> value, float min, float max, float x, float y, float size, float thickness, int color) {
		super(name, value, x, y, color);
		this.min = min;
		this.max = max;
		this.type = type;
		this.size = size;
		this.thickness = thickness;
	}

	/**
	 * Set the maximum expected value of the float.
	 *
	 * @param max New maximum value
	 */
	public void setMax(float max) {
		this.max = max;
	}

	@Override
	public int getColor() {
		float flash = (float)((.1 * Math.sin((v.millis()) / 100F)) + 0.9);
		if(getValue() > max || getValue() < min) {
			return v.color(255, 0, 0, 255 * flash);
		}
		else {
			return UI_COLOR;
		}
	}

	@Override
	public void draw() {
		switch(type) {
		case VERTICAL:
			drawVertical();
			break;
		case HORIZONTAL:
			drawHorizontal();
			break;
		default:
			drawRadial();
			break;
		}
	}

	/**
	 * Draw a vertical meter.
	 */
	private void drawVertical() {
		v.pushStyle();
		v.fill(0);
		v.stroke(getColor());
		v.rectMode(CENTER);
		v.rect(getX(), getY(), thickness, size);

		// Fill indicator
		v.rectMode(CORNERS);
		v.fill(getColor());
		v.rect(getX() - thickness / 2, (getY() + size / 2) - (percentage() * size), getX() + thickness / 2, getY() + size / 2);

		// Label
		v.textAlign(CENTER, CENTER);
		v.text(getName(), getX(), getY() - (size / 2 + 20));
		v.text(String.format("%.2f", getValue()), getX(), getY() + (size / 2 + 20));
		v.popStyle();
	}

	/**
	 * Draw a horizontal meter.
	 */
	private void drawHorizontal() {
		// TODO
	}

	/**
	 * Draw a radial meter.
	 */
	private void drawRadial() {
		// Draw outer edge
		v.pushStyle();
		v.fill(0);
		v.stroke(getColor());
		v.arc(getX(), getY(), size, size, PI / 2 + QUARTER_PI, PI * 2.5F - QUARTER_PI, PIE);

		// Fill indicator
		v.fill(getColor());
		v.arc(getX(), getY(), size, size, PI / 2 + QUARTER_PI, PI / 2 + QUARTER_PI + (QUARTER_PI * 6 * percentage()), PIE);

		//Draw inner edge and cover indicator center
		v.fill(0);
		v.arc(getX(), getY(), size - thickness, size - thickness, PI / 2 + QUARTER_PI, PI * 2.5F - QUARTER_PI, OPEN);

		// Label
		v.fill(getColor());
		v.textAlign(CENTER, CENTER);
		v.text(getName(), getX(), getY() + (size / 2 - 10));
		v.textSize(20);
		v.text(String.format("%.2f", getValue()), getX(), getY());
		v.popStyle();
	}

	/**
	 * Helper function to get the percentage of the maximum that the current value represents.
	 *
	 * @return percentage of the maximum that the current value represents.
	 */
	private float percentage() {
		return Math.max(0, Math.min(1, (getValue() / max)));
	}

	public enum IndicatorType {
		VERTICAL,
		HORIZONTAL,
		RADIAL
	}
}
