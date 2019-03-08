package vekta.mission;

import vekta.item.Item;
import vekta.object.SpaceObject;

public class ObtainItemObjective extends Objective {
	private final Item item;

	public ObtainItemObjective(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}
	
	@Override
	public String getName() {
		return "Obtain " + getItem().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}

	@Override
	public void onAddItem(Item item) {
		if(item == getItem()) {
			complete();
		}
	}
}
