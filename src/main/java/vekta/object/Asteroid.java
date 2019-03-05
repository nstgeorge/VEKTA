package vekta.object;

import processing.core.PVector;
import vekta.terrain.Terrain;

public class Asteroid extends TerrestrialPlanet {
	public Asteroid(String name, float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(name, mass, density, terrain, position, velocity, color);
	}

	@Override
	public boolean impartsGravity() {
		return false;
	}
}
