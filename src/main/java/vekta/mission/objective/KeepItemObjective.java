package vekta.mission.objective;

import vekta.item.Item;
import vekta.mission.Mission;
import vekta.object.SpaceObject;

public class KeepItemObjective extends Objective {
	private final Item item;

	public KeepItemObjective(Item item) {
		this.item = item;
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

	@Override
	public void onMissionStatus(Mission mission) {
		if(getMissions().contains(mission)) {
			for(Objective objective : mission.getObjectives()) {
				if(!objective.getStatus().isDone() && objective != this) {
					return;
				}
			}
			complete();
		}
	}
}
