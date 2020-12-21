package vekta.terrain.feature;

import vekta.object.planet.Planet;

import java.util.ArrayList;

public class IceFeature extends Feature {
	@Override
	public String getName() {
		return "Glacial";
	}

	public IceFeature(Planet planet) {
		super(planet);
	}

	@Override
	public ArrayList<String> getDescriptors() {
		return null;
	}

	@Override
	public Feature updateOrReplace() {
		Planet planet = getPlanet();
		if(planet.getTemperature() > 273) {
			return new OceanFeature(getPlanet());
		}
		return this;
	}
}
