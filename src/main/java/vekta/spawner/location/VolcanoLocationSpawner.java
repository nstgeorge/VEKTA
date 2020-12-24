package vekta.spawner.location;

import vekta.Resources;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

public class VolcanoLocationSpawner extends ProxyLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return .1F;
	}

	@Override
	public String chooseData() {
		return Resources.generateString("volcano");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.getData();
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return "You fly above the volcano.";
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add("Volcanoes");
	}
}
