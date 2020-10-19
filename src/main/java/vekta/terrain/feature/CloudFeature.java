package vekta.terrain.feature;

import vekta.object.planet.Planet;

import java.util.ArrayList;

public class CloudFeature extends Feature {
	public CloudFeature(Planet planet) {
		super(planet);
	}

	@Override
	public String getName() {
		return "Cloudy";
	}

	@Override
	public ArrayList<String> getDescriptors() {
		return null;
	}

	@Override
	public Feature updateOrReplace() {
		return this;
	}
}
