package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;

import java.util.Set;

public class GasGiantTerrain extends Terrain {

	public GasGiantTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	protected void onSurveyTags(Set<String> tags) {
//		tags.add("Gas Giant");
		tags.add("Rings");
	}

	@Override
	public String getOverview() {
		return "You fly into the gas giant's swirling atmosphere.";
	}

	@Override
	public boolean isHabitable() {
		return false;
	}
}
