package vekta.mission.objective;

import vekta.object.SpaceObject;

public class DestroyObjective extends Objective {
	private final SpaceObject target;

	public DestroyObjective(SpaceObject target) {
		if(target == null) {
			throw new RuntimeException("Target cannot be null");
		}

		this.target = target;
	}

	@Override
	public String getName() {
		return "Destroy " + target.getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		if(target.isDestroyed() && !getStatus().isDone()) {
			complete();
		}
		return target;
	}
}
