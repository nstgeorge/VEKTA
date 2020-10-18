package vekta.object.planet;

import processing.core.PVector;
import vekta.world.RenderLevel;

import java.util.Random;

import static vekta.Vekta.v;

public class Star extends Planet {
	private static final float LIGHT_RESOLUTION = 20;

	private static final int MAX_ORANGE = v.color(243, 157, 33);		// Coolest possible star color.
	private static final int MAX_BLUE = v.color(12, 98, 222);			// This must be the inverse of the orange color.

	private float temperature;
	private double luminosity;

	public Star(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		temperature = v.random(2400, 50000);

		luminosity = Math.pow(getRadius(), 2) * Math.pow(temperature, 4) * v.STEFAN_BOLTZMANN;

		// Override provided color with calculated one
		float gradientPosition = ((temperature - 2400) / 50000f);
		super.setColor(v.lerpColor(MAX_ORANGE, MAX_BLUE, gradientPosition));
		//		println("[Star] mass: " + getMass() + ", radius: " + getRadius());
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.INTERSTELLAR;
	}

	@Override
	public void drawDistant(float r) {
		drawRadialGradient(getColor(), v.color(0), r, r * 1.3F * (1 + (float)(luminosity / Math.pow(10, 36))));
		v.fill(0);
		super.drawDistant(r);
	}

	public double getLuminosity() {
		return luminosity;
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
