package vekta.item.category;

import vekta.item.Item;
import vekta.item.ItemType;

public class ItemTypeCategory extends ItemCategory {
	private final ItemType type;

	private static String getTypeName(ItemType type) {
		return (type.name().substring(0, 1).toUpperCase() + type.name().substring(1).toLowerCase()).replace("_", " ");
	}

	public ItemTypeCategory(ItemType type) {
		this(getTypeName(type), type);
	}

	public ItemTypeCategory(String name, ItemType type) {
		super(name);
		this.type = type;
	}

	public ItemType getType() {
		return type;
	}

	@Override
	public boolean isIncluded(Item item) {
		return item.getType() == getType();
	}
}
