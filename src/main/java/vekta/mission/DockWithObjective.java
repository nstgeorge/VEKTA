package vekta.mission;

import vekta.object.SpaceObject;
import vekta.object.ship.Ship;

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
	public void onDock(Ship ship) {
		if(ship == getSpaceObject()) {
			complete();
		}
	}
}
