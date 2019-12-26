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
		return new TerrestrialKnowledge(ObservationLevel.VISITED, getWorld().findRandomObject(TerrestrialPlanet.class));
	}
}
