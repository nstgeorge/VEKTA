package vekta.spawner.item;

import vekta.Resources;
import vekta.item.ArtifactItem;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.spawner.ItemGenerator;

public class ArtifactItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .2F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.LEGENDARY;
	}

	@Override
	public Item create() {
		return randomArtifactItem();
	}

	public static ArtifactItem randomArtifactItem() {
		String[] data = Resources.generateString("item_artifact").split(":", 2);
		String name = data[0].trim();
		String description = data[1].trim().replace("*", name);
		return new ArtifactItem(name, description, ItemType.LEGENDARY);
	}
}
