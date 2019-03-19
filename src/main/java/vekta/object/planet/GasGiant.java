package vekta.object.planet;

import processing.core.PShape;
import processing.core.PVector;

import java.io.ObjectStreamException;

import static processing.core.PConstants.*;
import static vekta.Vekta.v;

/**
 * Gas giant planet variant
 */
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
		ringDistances = new float[(int)v.random(2, 7)];
		ringColors = new int[ringDistances.length];
		float d = v.random(1.5F, 3);
		for(int i = 0; i < ringDistances.length; i++) {
			ringDistances[i] = d *= v.random(1.01F, 1.5F);
			ringColors[i] = v.lerpColor(20, 70, v.random(1));
		}
		maxRadius = d;

		setupShape();
	}

	private void setupShape() {
		v.ellipseMode(CORNER);
		v.noFill();
		v.strokeWeight(1);

		rings = new PShape[ringDistances.length];
		for(int i = 0; i < ringDistances.length; i++) {
			float rd = ringDistances[i];
			v.stroke(ringColors[i]);
			PShape ring = v.createShape(ELLIPSE, 0, 0, rd * 2, rd * 2 * ringRatio);
			rings[i] = ring;
		}
		
		v.ellipseMode(CENTER);
	}

	@Override
	protected Object readResolve() throws ObjectStreamException {
		setupShape();
		return super.readResolve();
	}

	@Override
	public float getOnScreenRadius(float r) {
		return r * maxRadius;
	}

	@Override
	public void drawNearby(float r) {
		super.drawNearby(r);

		v.scale(r);
		for(PShape ring : rings) {
			ring.setStrokeWeight(1 / r);
			v.shape(ring);
		}
	}
}
