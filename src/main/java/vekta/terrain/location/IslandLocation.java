package vekta.terrain.location;

import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;

import java.util.Set;

import static vekta.Vekta.v;

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
		if(getPlanet().getTemperatureCelsius() <= 0) {
			return "You fly above the vast ice sheets covering this planet.";
		}
		return "You fly above the planet's ocean.";
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		tags.add("Islands");
	}

	public boolean isVisitable(Player player) {
		return false;
	}
}
