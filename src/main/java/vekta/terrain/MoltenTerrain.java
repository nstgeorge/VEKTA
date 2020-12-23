package vekta.terrain;

import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;

import java.util.Set;

import static vekta.Vekta.v;

public class MoltenTerrain extends Terrain {

	private final float temperature = v.random(300, 1000);

	public MoltenTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		tags.add("Molten Surface");
	}

	@Override
	public String getOverview() {
		return "The entire planet is covered in molten lava.";
	}

	@Override
	public boolean isHabitable() {
		return false;
	}

	@Override
	public void onOrbit(SpaceObject orbitObject) {
		getPlanet().setTemperatureKelvin(temperature);
	}
}
