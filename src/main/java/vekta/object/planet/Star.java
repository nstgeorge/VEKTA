package vekta.object.planet;

import processing.core.PVector;

import static vekta.Vekta.v;

public class Star extends Planet {
	private static final float LIGHT_RESOLUTION = 20;

	public Star(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

//		println("[Star] mass: " + getMass() + ", radius: " + getRadius());
	}

	@Override
	public void drawDistant(float r) {
		drawRadialGradient(getColor(), v.color(0), r, r * 1.3F);
		v.fill(0);
		super.drawDistant(r);
	}

	// Draws radial gradient. This abstraction isn't necessary, but it helps readability
	private void drawRadialGradient(int colorFrom, int colorTo, float innerRadius, float outerRadius) {
		for(float i = outerRadius; i >= innerRadius; i -= (outerRadius - innerRadius) / LIGHT_RESOLUTION) {
			int color = v.lerpColor(colorFrom, colorTo, (i - innerRadius) / (outerRadius - innerRadius));
			v.stroke(color);
			v.fill(color);
			v.ellipse(0, 0, i, i);
		}
	}
}
