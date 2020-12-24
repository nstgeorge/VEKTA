package vekta.spawner.location;

import vekta.Resources;
import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Map;
import java.util.Set;

import static java.lang.Math.max;

public class DisasterFeature extends ProxyLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return max(terrain.getPlanet().getMass() / 1e24F, 1) * .05F;
	}

	@Override
	public String chooseData() {
		return Resources.generateString("disaster_map");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.getData().split(":")[0].trim();
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return location.getData().split(":")[1].trim();
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add(getName(location));
	}
}
