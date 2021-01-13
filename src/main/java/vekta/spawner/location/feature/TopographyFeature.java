package vekta.spawner.location.feature;

import vekta.Resources;
import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static vekta.Vekta.v;

public class TopographyFeature extends FeatureLocationSpawner<String[]> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return .5F;
	}

	@Override
	public String[] chooseData(Terrain terrain) {
		Set<String> tags = new HashSet<>();
		int ct = (int)v.random(3) + 1;
		for(int i = 0; i < ct; i++) {
			tags.add(Resources.generateString("topography"));
		}
		return tags.toArray(new String[0]);
	}

	@Override
	public String getName(ProxyLocation<String[]> location) {
		return "Topography";
	}

	@Override
	public void onSurveyTags(ProxyLocation<String[]> location, Set<String> tags) {
		tags.addAll(Arrays.asList(location.getData()));
	}
}
