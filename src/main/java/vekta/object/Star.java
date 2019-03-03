package vekta.object;

import processing.core.PVector;

import static vekta.Vekta.v;

public class Star extends Planet {
	public Star(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);
	}

	@Override
	public boolean isHabitable() {
		return false;
	}

	@Override
	public void draw() {
		drawRadialGradient(position, super.getColor(), v.color(0), getRadius(), getRadius() * 1.5F);
		super.draw();
	}

	// Draws radial gradient. This abstraction isn't necessary, but it helps readability
	private void drawRadialGradient(PVector position, int colorFrom, int colorTo, float innerRadius, float outerRadius) {
		for(float i = outerRadius; i >= innerRadius; i -= 1) {
			int color = v.lerpColor(colorFrom, colorTo, (i - innerRadius) / (outerRadius - innerRadius));
			v.stroke(color);
			v.fill(color);
			v.ellipse(position.x, position.y, i, i);
		}
	}
}
