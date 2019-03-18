package vekta.spawner.world;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.WorldGenerator;
import vekta.terrain.LandingSite;

import static vekta.Vekta.getWorld;

public abstract class ShipSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public RenderLevel getSpawnLevel() {
		return RenderLevel.PARTICLE;
	}

	@Override
	public RenderLevel getObjectLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(orbit instanceof TerrestrialPlanet && ((TerrestrialPlanet)orbit).isHabitable()) {
			LandingSite site = ((TerrestrialPlanet)orbit).getLandingSite();
			spawn(site, pos);
		}
	}

	public abstract void spawn(LandingSite site, PVector pos);
}