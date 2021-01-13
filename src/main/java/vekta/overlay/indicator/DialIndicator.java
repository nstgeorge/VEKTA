package vekta.overlay.indicator;

import processing.core.PVector;

import javax.lang.model.type.NullType;
import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PConstants.*;
import static vekta.Vekta.v;

/**
 * Represents an unbounded vector value to the player. Best used to represent a heading.
 */
public class DialIndicator extends Indicator<PVector> implements Serializable {

	// TODO: Add a RADIUS parameter
	private static final int RADIUS = 50;

	public DialIndicator(String name, DynamicValue<PVector> value, float x, float y, int color) {
		super(name, value, x, y, color);
	}

	@Override
	public void draw() {
		float x = getX();
		float y = getY();

		v.pushStyle();
		v.strokeWeight(2);
		v.fill(0);
		v.stroke(getColor());
		v.ellipse(x, y, RADIUS, RADIUS);
		v.fill(100, 100, 100);
		v.textAlign(CENTER);
		v.textSize(14);
		v.text(getName(), x, y + RADIUS / 2f);
		v.textAlign(LEFT);
		v.textSize(16);
		v.stroke(getColor());
		drawArrow(getValue(), (int)(RADIUS * .8), x, y);
		v.popStyle();
	}

	/**
	 * Draw the arrow of the dial.
	 *
	 * @param heading heading which the arrow points to
	 * @param length  length of the arrow
	 * @param x    Center point (x) of the arrow -- where it's "pinned"
	 * @param y    Center point (y) of the arrow -- where it's "pinned"
	 */
	private void drawArrow(PVector heading, float length, float x, float y) {
		heading.setMag(length);
		float angle = heading.heading();
		PVector endpoint = new PVector(cos(angle), sin(angle));
		endpoint.mult(length);

		v.beginShape(TRIANGLE);
		v.fill(0);
		v.vertex(x + endpoint.x, y + endpoint.y);
		v.vertex(x - (endpoint.x * 2) + cos(angle - .15F) * length, y - (endpoint.y * 2) + sin(angle - .15F) * length);
		v.vertex(x - (endpoint.x * 2) + cos(angle + .15F) * length, y - (endpoint.y * 2) + sin(angle + .15F) * length);
		v.endShape(CLOSE);
		//v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle - .3F) * (length * .8F), locY + sin(angle - .3F) * (length * .8F));
		//v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle + .3F) * (length * .8F), locY + sin(angle + .3F) * (length * .8F));
	}

}
