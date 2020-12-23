package vekta.spawner.location.feature;

import vekta.Resources;
import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

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
	public String chooseData() {
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
}
