package vekta.spawner.item;

import vekta.Faction;
import vekta.Resources;
import vekta.item.ClothingItem;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;

import static vekta.Vekta.v;

public class ClothingItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof ClothingItem;
	}

	@Override
	public Item create() {
		return v.chance(.2F)
				? createDisguiseItem(FactionGenerator.randomFaction())
				: createClothingItem();
	}

	public static Item createClothingItem() {
		return new ClothingItem(Resources.generateString("item_clothing_common"), ItemType.COMMON, null);
	}

	public static ClothingItem createDisguiseItem(Faction faction) {
		return new ClothingItem(Resources.generateString("item_clothing_rare"), ItemType.RARE, faction);
	}
}
