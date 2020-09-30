package vekta.item;

public class JunkItem extends Item {
	private String name;

	public JunkItem(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemType getType() {
		return ItemType.JUNK;
	}

	@Override
	public int getMass() {
		return 5;
	}
}
