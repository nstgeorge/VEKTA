package vekta.object.planet;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.terrain.Terrain;

import static processing.core.PConstants.TWO_PI;
import static vekta.Vekta.v;

public class Asteroid extends TerrestrialPlanet {
	private final float angle;
	private final float skew;

	public Asteroid(String name, float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(name, mass, density, terrain, position, velocity, color);

		angle = v.random(TWO_PI);
		skew = v.random(.5F, 1);
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public boolean impartsGravity() {
		return false;
	}

	@Override
	public void drawNearby(float r) {
		v.rotate(angle);

		v.ellipse(0, 0, r, r * skew);
	}
}
