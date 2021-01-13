package vekta.spawner.location;

import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

public class CraterLocationSpawner extends ProxyLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return 1e22F / terrain.getPlanet().getMass();
	}

	@Override
	public String chooseData(Terrain terrain) {
		return null;
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return "Crater";
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return "You land in the largest crater you can find.";
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add("Craters");
	}
}
