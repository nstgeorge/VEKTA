package vekta.spawner.location.feature;

import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

public class StormsFeature extends FeatureLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return terrain.getPlanet().getAtmosphereDensity() >= .5F;
	}

	@Override
	public float getChance(Terrain terrain) {
		return Math.max(terrain.getPlanet().getAtmosphereDensity(), 1) * .5F;
	}

	@Override
	public String chooseData() {
		return null;
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.atm() > 100 ? "Superstorms" : location.atm() > .1 ? "Storms" : "Winds";
	}
}
