package vekta.spawner.location;

import vekta.terrain.location.ProxyLocation;

import java.io.Serializable;
import java.util.Set;

public abstract class FeatureLocationSpawner<T extends Serializable> extends ProxyLocationSpawner<T> {

	public String getOverview(ProxyLocation<T> location) {
		return null;
	}

	public boolean isVisitable(ProxyLocation<T> location) {
		return false;
	}

	public void onSurveyTags(ProxyLocation<T> location, Set<String> tags) {
		tags.add(location.getName());
	}
}
