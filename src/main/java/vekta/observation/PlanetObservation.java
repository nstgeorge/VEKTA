package vekta.observation;

import vekta.object.planet.Planet;

public class PlanetObservation implements Observation {
	private final Planet planet;
	private ObservationLevel level;

	public PlanetObservation(Planet planet, ObservationLevel level) {
		this.planet = planet;
		this.level = level;
	}

	public Planet getPlanet() {
		return planet;
	}

	public ObservationLevel getLevel() {
		return level;
	}
}
