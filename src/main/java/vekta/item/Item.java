package vekta.item;

public class Item implements Comparable<Item> {
	private final String name;
	private final ItemType type;

	public Item(String name, ItemType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public ItemType getType() {
		return type;
	}

	@Override
	public int compareTo(Item other) {
		return this.getName().compareTo(other.getName());
	}
}
