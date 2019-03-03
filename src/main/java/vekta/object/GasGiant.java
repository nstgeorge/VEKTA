package vekta.object;

import processing.core.PVector;
import vekta.Vekta;

import static vekta.Vekta.getInstance;

/**
 * Gas giant planet variant
 */
public class GasGiant extends Planet {

	private final float ringAngle;
	private final float ringRatio;
	private final float[] ringDistances;

	public GasGiant(float mass, float density, PVector position, PVector velocity, int color) {
		super(mass, density, position, velocity, color);
		ringAngle = v.random(360);
		ringRatio = v.random(.1F, 1);
		ringDistances = new float[(int)v.random(2, 7)];
		float d = v.random(1.5F, 3);
		for(int i = 0; i < ringDistances.length; i++) {
			ringDistances[i] = d *= v.random(1.01F, 1.5F);
		}
	}

	@Override
	public boolean isHabitable() {
		return false;
	}

	@Override
	public boolean doesSplitOnDestroy() {
		return false;
	}

	@Override
	public void draw() {
		super.draw();

		Vekta v = getInstance();
		v.stroke(v.color(50));
		v.noFill();
		v.pushMatrix();
		v.translate(position.x, position.y);
		v.rotate(ringAngle);
		for(float d : ringDistances) {
			float r = getRadius() * d;
			v.ellipse(0, 0, r, r * ringRatio);
		}
		v.popMatrix();
	}
}
