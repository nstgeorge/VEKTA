package vekta.spawner.knowledge;

import vekta.Resources;
import vekta.knowledge.Knowledge;
import vekta.knowledge.TopicKnowledge;
import vekta.spawner.KnowledgeGenerator;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public class TopicKnowledgeSpawner implements KnowledgeGenerator.KnowledgeSpawner {
	@Override
	public float getWeight() {
		return 0; // Disabled for now
	}

	@Override
	public Knowledge create() {
		return randomTopicKnowledge();
	}

	public static TopicKnowledge randomTopicKnowledge() {
		String[] parts = Resources.generateString("topic").split(":");
		return new TopicKnowledge(parts[0].trim(), parts[1].trim(), round(v.random(2, 10)));
	}
}
