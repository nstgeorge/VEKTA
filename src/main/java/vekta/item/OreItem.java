package vekta.item;

public class OreItem extends Item {
	private final Item refined;

	public OreItem(String name, Item refined, ItemType type) {
		super(name, type);
		
		this.refined = refined;
	}

	public Item getRefined() {
		return refined;
	}
}
