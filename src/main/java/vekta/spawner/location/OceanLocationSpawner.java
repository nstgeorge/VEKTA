package vekta.spawner.location;

import vekta.Resources;
import vekta.spawner.LocationGenerator;
import vekta.terrain.Terrain;
import vekta.terrain.location.IslandLocation;
import vekta.terrain.location.OceanLocation;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

import static vekta.Vekta.v;

public class OceanLocationSpawner implements LocationGenerator.LocationSpawner {

	@Override
	public boolean isValid(Terrain terrain) {
		return terrain.getPlanet().getMass() >= 1e24;
	}

	@Override
	public float getChance(Terrain terrain) {
		return .8F;
	}

	@Override
	public void spawn(Terrain terrain) {
		terrain.addPathway(new OceanLocation(terrain.getPlanet(), "Ocean"));

		if(v.chance(.5F)) {
			terrain.addPathway(new IslandLocation(terrain.getPlanet(), "Island"));
		}
	}
}
