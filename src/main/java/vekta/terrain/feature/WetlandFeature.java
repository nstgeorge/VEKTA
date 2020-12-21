package vekta.terrain.feature;

import vekta.object.planet.Planet;

import java.util.ArrayList;

public class WetlandFeature extends Feature {
	public WetlandFeature(Planet planet) {
		super(planet);
	}

	@Override
	public String getName() {
		return "Wetlands";
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
