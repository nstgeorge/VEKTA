package vekta.terrain.condition;

import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;

import java.util.function.Function;

public enum PlanetQuantity implements Condition<TerrestrialPlanet> {
	// Properties
	TEMP_KELVIN(SpaceObject::getTemperatureKelvin),
	TEMP_CELSIUS(SpaceObject::getTemperatureCelsius),
	ATMOSPHERE(TerrestrialPlanet::getAtmosphereDensity),
	ATMOSPHERE_LOG(terrestrialPlanet -> (float)Math.log10(terrestrialPlanet.getAtmosphereDensity())),

	// Convenience
	HOT(planet -> (planet.getTemperatureCelsius() - 100) / 50),
	COLD(planet -> (100 - planet.getTemperatureCelsius()) / 50),
	HABITABLE(planet -> planet.getTemperatureCelsius() > -100 && planet.getTemperatureCelsius() < 100 ? 1f : 0f);//TODO Gaussian curve or something like that

	private final Function<TerrestrialPlanet, Float> query;

	PlanetQuantity(Function<TerrestrialPlanet, Float> query) {
		this.query = query;
	}

	@Override
	public float getScore(TerrestrialPlanet planet) {
		return query.apply(planet);
	}
}
