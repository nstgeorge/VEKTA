package vekta.spawner.location;

import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

import static java.lang.Math.max;

public class StormLocationSpawner extends ProxyLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return terrain.getPlanet().getAtmosphereDensity() >= .1F;
	}

	@Override
	public float getChance(Terrain terrain) {
		return max(1, terrain.getPlanet().getAtmosphereDensity()) * max(1, 10 / terrain.getPlanet().getRotationHours());
	}

	@Override
	public String chooseData(Terrain terrain) {
		return null;
	}

	@Override
	public void setup(ProxyLocation<String> location, Terrain terrain) {
		location.setWittyText(null);
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.getPlanet().getName() + " " + getTagName(location);
	}

	public String getTagName(ProxyLocation<String> location) {
		return location.atm() > 100 ? "Superstorm" : location.atm() > .2 ? "Storm" : "Gale";
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return "Turbulence rattles your spacecraft as you venture into the storm.";
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add(location.getName() + "s");
	}
}
