package vekta.item;

public class BasicItem extends Item {
	private final String name;
	private final ItemType type;

	public BasicItem(String name, ItemType type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemType getType() {
		return type;
	}

	@Override
	public int getMass() {
		return 10;
	}
}
