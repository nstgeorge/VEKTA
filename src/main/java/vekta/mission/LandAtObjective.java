package vekta.mission;

import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

public class LandAtObjective extends Objective {
	private final SpaceObject object;

	public LandAtObjective(SpaceObject object) {
		if(object == null) {
			throw new RuntimeException("Landing target cannot be null");
		}
		this.object = object;
	}

	@Override
	public String getName() {
		return "Land at " + getSpaceObject().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return object;
	}

	@Override
	public void onLand(LandingSite site) {
		if(site.getParent() == getSpaceObject()) {
			complete();
		}
	}
}
