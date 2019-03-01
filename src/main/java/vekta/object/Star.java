package vekta.object;

import processing.core.PVector;
import vekta.Vekta;

import static vekta.Vekta.getInstance;

public class Star extends Planet {
	public Star(float mass, float density, PVector position, PVector velocity, int color) {
		super(mass, density, position, velocity, color);
	}

	@Override
	public boolean isHabitable() {
		return false;
	}

	public void draw() {
		drawRadialGradient(position, super.getColor(), getInstance().color(0), getRadius(), getRadius() * 1.5F);
		super.draw();
	}

	// Draws radial gradient. This abstraction isn't necessary, but it helps readability
	private void drawRadialGradient(PVector position, int colorFrom, int colorTo, float innerRadius, float outerRadius) {
		Vekta v = getInstance();
		for(float i = outerRadius; i >= innerRadius; i -= 1) {
			int color = v.lerpColor(colorFrom, colorTo, (i - innerRadius) / (outerRadius - innerRadius));
			v.stroke(color);
			v.fill(color);
			v.ellipse(position.x, position.y, i, i);
		}
	}
}
