package vekta.object.planet;

import processing.core.PVector;
import vekta.world.RenderLevel;

import static processing.core.PApplet.println;
import static vekta.Vekta.v;

public class Star extends Planet {
	private static final float LIGHT_RESOLUTION = 20;

	private static final int MAX_ORANGE = v.color(243, 157, 33);        // Coolest possible star color.
	private static final int MAX_BLUE = v.color(12, 98, 222);            // This must be the inverse of the orange color.

	private final float luminosity;

	public Star(String name, float mass, float density, float temperature, PVector position, PVector velocity) {
		super(name, mass, density, position, velocity, 255);

		setTemperatureKelvin(temperature);

		luminosity = (float)(Math.pow(getRadius(), 2) * Math.pow(temperature, 4) * v.STEFAN_BOLTZMANN);

		// Override provided color with calculated one
		float gradientPosition = ((temperature - 2400) / 50000f);
		setColor(v.lerpColor(MAX_ORANGE, MAX_BLUE, gradientPosition));
		println("[Star] mass: " + getMass() + ", radius: " + getRadius() + ", temperature: " + getTemperatureKelvin() + ", luminosity: " + getLuminosity());
	}

	public float getLuminosity() {
		return luminosity;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.INTERSTELLAR;
	}

	//	@Override
	//	public void drawNearby(float r) {
	//		super.drawNearby(r);
	//
	//		drawRadialGradient(getColor(), v.color(0), r, r * 1.3f * (1 + (float)(luminosity / 1e36)));
	//		v.fill(0);
	//	}

	//	// Draws radial gradient. This abstraction isn't necessary, but it helps readability
	//	private void drawRadialGradient(int colorFrom, int colorTo, float innerRadius, float outerRadius) {
	//		for(float i = outerRadius; i >= innerRadius; i -= (outerRadius - innerRadius) / LIGHT_RESOLUTION) {
	//			int color = v.lerpColor(colorFrom, colorTo, (i - innerRadius) / (outerRadius - innerRadius));
	//			v.stroke(color);
	//			v.fill(color);
	//			v.ellipse(0, 0, i, i);
	//		}
	//	}
}
