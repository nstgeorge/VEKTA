package vekta.object.planet;

import processing.core.PVector;
import vekta.terrain.AsteroidTerrain;
import vekta.terrain.BlackHoleTerrain;
import vekta.world.RenderLevel;
import vekta.terrain.Terrain;

import static processing.core.PConstants.TWO_PI;
import static vekta.Vekta.v;

public class Asteroid extends TerrestrialPlanet {
	private final float angle;
	private final float skew;

	public Asteroid(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		angle = v.random(TWO_PI);
		skew = v.random(.5F, 1);
	}

	@Override
	protected Terrain chooseTerrain() {
		return new AsteroidTerrain(this);
	}

	@Override
	public float getValueScale() {
		return .5F;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public RenderLevel getDespawnLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public boolean impartsGravity() {
		return false;
	}

	@Override
	public void drawNearby(float r) {
		if(r < .1F) {
			r = .1F;
		}
		v.strokeWeight(1);
		v.rotate(angle);
		v.ellipse(0, 0, r, r * skew);
	}
}
