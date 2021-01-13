package vekta.spawner.location;

import vekta.Resources;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

public class ShrineLocationSpawner extends ProxyLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return .02F;
	}

	@Override
	public String chooseData(Terrain terrain) {
		return Resources.generateString("shrine_map");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.getPlanet().getName() + " " + location.getData().split(":")[0].trim();
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return location.getData().split(":")[1].trim();
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add("Relics");
	}
}
