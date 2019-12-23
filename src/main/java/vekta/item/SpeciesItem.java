package vekta.item;

public class SpeciesItem extends Item {
	private String name;

	public SpeciesItem(String name) {
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
