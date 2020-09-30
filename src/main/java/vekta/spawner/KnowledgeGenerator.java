package vekta.spawner;

import vekta.Resources;
import vekta.knowledge.Knowledge;
import vekta.knowledge.KnowledgeSource;

import java.util.Arrays;

public class KnowledgeGenerator {
	private static final KnowledgeSpawner[] SPAWNERS = Resources.findSubclassInstances(KnowledgeSpawner.class);

	public static Knowledge createKnowledge() {
		return Weighted.random(SPAWNERS).create();
	}

	public static Knowledge createKnowledge(KnowledgeSource source) {
		KnowledgeSpawner[] spawners = Arrays.stream(SPAWNERS)
				.filter(s -> s.isValid(source))
				.toArray(KnowledgeSpawner[]::new);
		
		return Weighted.random(spawners).create(source);
	}

	public interface KnowledgeSpawner extends Weighted {
		default boolean isValid(KnowledgeSource source) {
			return true;
		}

		default Knowledge create(KnowledgeSource source) {
			return create();
		}

		Knowledge create();
	}
}
