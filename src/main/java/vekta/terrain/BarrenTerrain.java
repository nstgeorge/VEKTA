package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.EcosystemGenerator;

import java.util.Set;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public class BarrenTerrain extends Terrain {

	public BarrenTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		tags.add("Barren");
	}

	@Override
	public String getOverview() {
		return "This planet is completely devoid of anything remotely interesting.";
	}

	@Override
	public boolean isHabitable() {
		return false;
	}
}
