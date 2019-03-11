package vekta.item;

public abstract class ItemCategory {
	private final String name;

	public ItemCategory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract boolean isIncluded(Item item);
}
