package vekta.mission.objective;

import vekta.item.Item;
import vekta.item.category.ItemCategory;
import vekta.object.SpaceObject;

public class ObtainItemCategoryObjective extends Objective {
	private final ItemCategory category;

	public ObtainItemCategoryObjective(ItemCategory category) {
		this.category = category;
	}

	public ItemCategory getCategory() {
		return category;
	}

	@Override
	public String getName() {
		return "Obtain " + getCategory().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}

	@Override
	public void onAddItem(Item item) {
		if(getCategory().isIncluded(item)) {
			complete();
		}
	}
}
