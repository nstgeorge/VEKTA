package vekta.spawner.location;

import vekta.spawner.LocationGenerator;
import vekta.spawner.location.ProxyLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
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
