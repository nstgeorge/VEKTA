package vekta.terrain.location;

import vekta.object.planet.TerrestrialPlanet;

import java.util.Set;

public class IslandLocation extends Location {

	private final String name;

	public IslandLocation(TerrestrialPlanet planet, String name) {
		super(planet);

		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	//	@Override
	//	public boolean isVisitable(Player player) {
	//		return terrain.getPlanet().getTemperatureCelsius() < 100;
	//	}

	@Override
	public String getOverview() {
		return "This planet's ocean is filled with islands.";
	}

	@Override
	public boolean isHabitable() {
		return getPlanet().getTerrain().isHabitable();
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		tags.add("Islands");
	}

	public boolean isVisitable() {
		return false;
	}
}
