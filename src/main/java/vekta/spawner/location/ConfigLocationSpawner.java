package vekta.spawner.location;

import vekta.Resources;
import vekta.config.LocationConfig;
import vekta.spawner.LocationGenerator;
import vekta.terrain.Terrain;
import vekta.terrain.location.ConfigLocation;

import java.util.List;

public class ConfigLocationSpawner implements LocationGenerator.LocationSpawner {

	private static final List<LocationConfig> CONFIGS = Resources.getConfigs(LocationConfig.class);

	@Override
	public boolean isValid(Terrain terrain) {
		return false; // TODO: reintroduce
	}

	@Override
	public void spawn(Terrain terrain) {

		for(LocationConfig config : CONFIGS) {

			if(config.enabled.asBoolean(terrain)) {
				terrain.addPathway(new ConfigLocation(terrain.getPlanet(), config));
			}
		}
	}
}
