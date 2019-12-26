package vekta.terrain;

import vekta.spawner.EcosystemGenerator;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public class OceanicTerrain extends Terrain {

	public OceanicTerrain() {
		addFeature("Oceanic");
		
		if(v.chance(.8F)) {
			addFeature("Atmosphere");
		}

		if(v.chance(.2F)) {
			addFeature("Bioluminescent");

			EcosystemGenerator.populateEcosystem(getEcosystem(), round(v.random(3, 15)));
		}
	}

	@Override
	public String getOverview() {
		return "You can't find anywhere to land; a vast ocean covers this planet.";
	}
}
