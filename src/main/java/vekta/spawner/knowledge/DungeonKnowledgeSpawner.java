package vekta.spawner.knowledge;

import vekta.dungeon.Dungeon;
import vekta.knowledge.DungeonKnowledge;
import vekta.knowledge.Knowledge;
import vekta.knowledge.ObservationLevel;
import vekta.spawner.DungeonGenerator;
import vekta.spawner.KnowledgeGenerator;

public class DungeonKnowledgeSpawner implements KnowledgeGenerator.KnowledgeSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public Knowledge create() {
		return randomDungeonKnowledge();
	}

	public static DungeonKnowledge randomDungeonKnowledge() {
		Dungeon dungeon = DungeonGenerator.randomDungeon();
		return new DungeonKnowledge(ObservationLevel.SCANNED, dungeon);
	}
}
