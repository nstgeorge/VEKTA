package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;

import java.util.Set;

public class AsteroidTerrain extends Terrain {

	public AsteroidTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	public String getOverview() {
		return "You carefully touch down on the asteroid.";
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
//		tags.add("Asteroid");
	}

	@Override
	public boolean isHabitable() {
		return false;
	}
}
