package vekta.spawner.knowledge;

import vekta.Resources;
import vekta.knowledge.Knowledge;
import vekta.knowledge.StoryKnowledge;
import vekta.spawner.KnowledgeGenerator;
import vekta.spawner.StoryGenerator;

public class StoryKnowledgeSpawner implements KnowledgeGenerator.KnowledgeSpawner {
	@Override
	public float getWeight() {
		return .2F;
	}

	@Override
	public Knowledge create() {
		return new StoryKnowledge(
				StoryGenerator.createStory(),
				Resources.generateString("item_knowledge"));
	}
}
