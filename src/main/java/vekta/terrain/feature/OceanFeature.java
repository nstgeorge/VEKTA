package vekta.terrain.feature;

import vekta.object.planet.Planet;

import java.util.ArrayList;

import static vekta.Vekta.v;

public class OceanFeature extends Feature {

	private final ArrayList<String> descriptors;

	public OceanFeature(Planet planet) {
		super(planet);

		descriptors = new ArrayList<>();

		if(v.chance(.02F)) {
			descriptors.add("Bioluminescent");
		}
	}

	@Override
	public String getName() {
		return "Oceanic";
	}

	@Override
	public ArrayList<String> getDescriptors() {
		return descriptors;
	}

	@Override
	public Feature updateOrReplace() {
		Planet planet = getPlanet();
		if(planet.getTemperature() > 373) {

		}
		else if(planet.getTemperature() > 300) {
			return new WetlandFeature(getPlanet());
		}
		else if(planet.getTemperature() < 280 && !descriptors.contains("Large icebergs")) {
			descriptors.add("Large icebergs");
		}
		else if(planet.getTemperature() < 273) {
			return new IceFeature(getPlanet());
		}
		return this;
	}
}
