package vekta.object.planet;

import processing.core.PShape;
import processing.core.PVector;

import static processing.core.PApplet.sq;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.ELLIPSE;
import static vekta.Vekta.v;

public class GasGiant extends Planet {
	private final float ringAngle;
	private final float ringRatio;
	private final float[] ringDistances;
	private final int[] ringColors;
	private final float maxRadius;

	private transient PShape[] rings;

	public GasGiant(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);
		ringAngle = v.random(360);
		ringRatio = v.random(.1F, 1);
		ringDistances = new float[(int)v.random(2, 5)];
		ringColors = new int[ringDistances.length];
		float d = v.random(1.5F, 3);
		for(int i = 0; i < ringDistances.length; i++) {
			ringDistances[i] = d *= v.random(1.01F, 1.5F);
			ringColors[i] = v.lerpColor(20, 70, v.random(1));
		}
		maxRadius = d;
	}

	public boolean isInsideRings(PVector pos) {
		float distSq = getPosition().sub(pos).magSq();
		return distSq <= sq(getRadius() * ringDistances[ringDistances.length - 1]);
	}

	@Override
	public float getOnScreenRadius(float r) {
		return r * maxRadius;
	}

	@Override
	public void drawNearby(float r) {
		super.drawNearby(r);

		// Initialize in draw loop for consistent rendering parameters
		if(rings == null) {
			rings = new PShape[ringDistances.length];
			v.shapeMode(CENTER);/// TODO: ensure that this fixes the ring rendering issue
			for(int i = 0; i < ringDistances.length; i++) {
				float rd = ringDistances[i];
				v.stroke(ringColors[i]);
				PShape ring = v.createShape(ELLIPSE, 0, 0, rd, rd * ringRatio);
				rings[i] = ring;
			}
		}

		v.scale(r);
		v.rotate(ringAngle);
		for(PShape ring : rings) {
			ring.setStrokeWeight(1 / r);
			v.shape(ring);
		}
	}
}
