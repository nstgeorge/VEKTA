package vekta.spawner.location.feature;

import vekta.Resources;
import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

public class CloudsFeature extends FeatureLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return terrain.getPlanet().getAtmosphereDensity() > .01F;
	}

	@Override
	public float getChance(Terrain terrain) {
		return terrain.getPlanet().getAtmosphereDensity() * 10;
	}

	@Override
	public String chooseData(Terrain terrain) {
		return Resources.generateString("cloud");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		String prefix = null;
		if(location.tempC() <= -200) {
			prefix = "Icy";
		}
		if(location.atm() > 10F) {
			prefix = "Thick";
		}
		if(location.atm() < .2F) {
			prefix = "Thin";
		}
		return (prefix != null ? prefix + " " : "") + location.getData();
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add(location.getName() + "s");
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return "Turbulence rattles your spacecraft as you venture into the storm.";
	}
}
