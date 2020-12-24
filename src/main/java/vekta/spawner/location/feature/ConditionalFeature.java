package vekta.spawner.location.feature;

import vekta.Resources;
import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

public class ConditionalFeature extends FeatureLocationSpawner<ConditionalFeatureData> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return .5F;
	}

	@Override
	public ConditionalFeatureData chooseData() {
		ConditionalFeatureData data = new ConditionalFeatureData();
		data.warm = Resources.generateString("feature_warm");
		data.cold = Resources.generateString("feature_cold");
		data.large = Resources.generateString("feature_large");
		data.tiny = Resources.generateString("feature_tiny");
		return data;
	}

	@Override
	public String getName(ProxyLocation<ConditionalFeatureData> location) {
		return "Topography";
	}

	@Override
	public void onSurveyTags(ProxyLocation<ConditionalFeatureData> location, Set<String> tags) {
		ConditionalFeatureData data = location.getData();
		if(location.tempC(-100, 10)) {
			tags.add(data.cold);
		}
		if(location.tempC(-10, 100)) {
			tags.add(data.warm);
		}
		if(location.mass() > 1e25) {
			tags.add(data.large);
		}
		if(location.mass() < 1e10) {
			tags.add(data.tiny);
		}
	}
}
