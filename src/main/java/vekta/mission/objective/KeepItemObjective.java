package vekta.mission.objective;

import vekta.item.Item;
import vekta.object.SpaceObject;

public class KeepItemObjective extends Objective {
	private final Item item;

	public KeepItemObjective(Item item) {
		this.item = item;
		
		this.optional();/// TODO: complete after becoming the only remaining objective
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String getName() {
		return "Keep " + getItem().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}
	
	@Override
	public void onRemoveItem(Item item) {
		if(item == getItem()) {
			cancel();
		}
	}
}
