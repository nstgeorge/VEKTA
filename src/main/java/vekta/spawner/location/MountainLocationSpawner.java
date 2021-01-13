package vekta.spawner.location;

import vekta.Resources;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

import static vekta.Vekta.v;

public class MountainLocationSpawner extends ProxyLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return .1F;
	}

	@Override
	public String chooseData(Terrain terrain) {
		String name = v.chance(.3f) ? Resources.generateString("lastname") : terrain.getName();
		if(v.chance(.2f)) {
			return "Mount " + name;
		}
		return name + " " + Resources.generateString("mountain_suffix");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.getData();
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return "The tallest mountain casts a shadow over your spacecraft.";
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add("Mountains");
	}
}
