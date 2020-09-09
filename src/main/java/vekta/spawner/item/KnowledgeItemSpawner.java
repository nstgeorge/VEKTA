package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.KnowledgeItem;
import vekta.knowledge.KnowledgeSource;
import vekta.spawner.ItemGenerator;
import vekta.spawner.KnowledgeGenerator;

public class KnowledgeItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .3F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.KNOWLEDGE;
	}

	@Override
	public Item create() {
		return randomKnowledgeItem();
	}

	public static String randomKnowledgeItemName() {
		return Resources.generateString("item_knowledge");
	}

	public static KnowledgeItem randomKnowledgeItem() {
		return new KnowledgeItem(randomKnowledgeItemName(), KnowledgeGenerator::createKnowledge);
	}

	public static KnowledgeItem randomKnowledgeItem(KnowledgeSource source) {
		return new KnowledgeItem(randomKnowledgeItemName(), () -> KnowledgeGenerator.createKnowledge(source));
	}
}
