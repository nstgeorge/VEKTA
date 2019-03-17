package vekta.item;

import java.io.Serializable;

public abstract class ItemCategory implements Serializable {
	private final String name;

	public ItemCategory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract boolean isIncluded(Item item);
}
