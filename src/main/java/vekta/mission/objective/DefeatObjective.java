package vekta.mission.objective;

import vekta.object.SpaceObject;

import static vekta.Vekta.getWorld;

public class DefeatObjective extends Objective {
	private final SpaceObject target;

	public DefeatObjective(SpaceObject target) {
		if(target == null) {
			throw new RuntimeException("Target cannot be null");
		}

		this.target = target;
	}

	@Override
	public String getName() {
		return "Defeat " + target.getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		if(target.isDestroyed() && !getStatus().isDone()) {
			cancel();
		}
		return target;
	}

	@Override
	public void onDestroyObject(SpaceObject object) {
		SpaceObject orbit = getSpaceObject();
		if(object == target && (orbit == null || getWorld().findOrbitObject(object) == orbit)) {
			complete();
		}
	}
}
