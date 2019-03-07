package vekta.object.planet;

import processing.core.PVector;

import static vekta.Vekta.v;

/**
 * Gas giant planet variant
 */
public class GasGiant extends Planet {

	private final float ringAngle;
	private final float ringRatio;
	private final float[] ringDistances;

	public GasGiant(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);
		ringAngle = v.random(360);
		ringRatio = v.random(.1F, 1);
		ringDistances = new float[(int)v.random(2, 7)];
		float d = v.random(1.5F, 3);
		for(int i = 0; i < ringDistances.length; i++) {
			ringDistances[i] = d *= v.random(1.01F, 1.5F);
		}
	}

	@Override
	public void drawDistant(float r) {
		super.drawDistant(r);

		v.stroke(v.color(50));
		v.noFill();
		v.rotate(ringAngle);
		for(float d : ringDistances) {
			float rd = r * d;
			v.ellipse(0, 0, rd, rd * ringRatio);
		}
	}
}
