package vekta.mission.objective;

import vekta.object.SpaceObject;
import vekta.sync.Sync;

import static vekta.Vekta.getWorld;

public class DestroyAroundObjective extends Objective {
	private final String name;
	private final Class<? extends SpaceObject> type;
	private @Sync SpaceObject orbit;

	public DestroyAroundObjective(String name, Class<? extends SpaceObject> type) {
		this(name, type, null);
	}

	public DestroyAroundObjective(String name, Class<? extends SpaceObject> type, SpaceObject orbit) {
		this.name = name;
		this.type = type;
		this.orbit = orbit;
	}

	public Class<? extends SpaceObject> getType() {
		return type;
	}

	@Override
	public String getName() {
		return name + /*(getSpaceObject() != null ? */" orbiting " + getSpaceObject().getName()/* : "")*/;
	}

	@Override
	public SpaceObject getSpaceObject() {
		if(orbit != null && orbit.isDestroyed()) {
			//			orbit = null;
			cancel();
		}
		return orbit;
	}
	
	@Override
	public void onDestroyObject(SpaceObject object) {
		SpaceObject orbit = getSpaceObject();
		if(type.isInstance(object) && /*(orbit == null || */getWorld().findOrbitObject(object) == orbit/*)*/) {
			complete();
		}
	}
}
