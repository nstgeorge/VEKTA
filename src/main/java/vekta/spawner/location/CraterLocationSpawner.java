package vekta.spawner.location;

import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

public class CraterLocationSpawner extends FeatureLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return 1e22F / terrain.getPlanet().getMass();
	}

	@Override
	public String chooseData() {
		return null;
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return "Craters";
	}
}
