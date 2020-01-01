package vekta.spawner.knowledge;

import vekta.knowledge.Knowledge;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.TerrestrialKnowledge;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.KnowledgeGenerator;

import static vekta.Vekta.getWorld;

public class TerrestrialKnowledgeSpawner implements KnowledgeGenerator.KnowledgeSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public Knowledge create() {
		return randomTerrestrialKnowledge();
	}

	public static TerrestrialKnowledge randomTerrestrialKnowledge() {
		TerrestrialPlanet planet = getWorld().findRandomObject(TerrestrialPlanet.class);
		return new TerrestrialKnowledge(ObservationLevel.SCANNED, planet);
	}
}
