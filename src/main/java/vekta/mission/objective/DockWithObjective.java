package vekta.mission.objective;

import vekta.object.SpaceObject;

public class DockWithObjective extends Objective {
	private final SpaceObject object;

	public DockWithObjective(SpaceObject object) {
		this.object = object;
	}

	@Override
	public String getName() {
		return "Dock at " + getSpaceObject().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return object;
	}

	@Override
	public void onDock(SpaceObject object) {
		if(object == getSpaceObject()) {
			complete();
		}
	}
}
