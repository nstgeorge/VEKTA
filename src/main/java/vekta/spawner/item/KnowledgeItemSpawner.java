package vekta.spawner.item;

import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.KnowledgeItem;
import vekta.knowledge.KnowledgeSource;
import vekta.spawner.ItemGenerator;
import vekta.spawner.KnowledgeGenerator;

public class KnowledgeItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.KNOWLEDGE;
	}

	@Override
	public Item create() {
		return randomKnowledgeItem();
	}

	public static KnowledgeItem randomKnowledgeItem() {
		return new KnowledgeItem(() -> KnowledgeGenerator.createKnowledge());
	}

	public static KnowledgeItem randomKnowledgeItem(KnowledgeSource source) {
		return new KnowledgeItem(() -> KnowledgeGenerator.createKnowledge(source));
	}
}
