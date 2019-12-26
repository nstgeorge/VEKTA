package vekta.spawner.knowledge;

import vekta.knowledge.Knowledge;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.SettlementKnowledge;
import vekta.spawner.KnowledgeGenerator;
import vekta.spawner.PersonGenerator;

public class SettlementKnowledgeSpawner implements KnowledgeGenerator.KnowledgeSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public Knowledge create() {
		return randomSettlementKnowledge();
	}

	public static SettlementKnowledge randomSettlementKnowledge() {
		return new SettlementKnowledge(ObservationLevel.VISITED, PersonGenerator.randomHome());
	}
}
