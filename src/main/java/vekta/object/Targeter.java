package vekta.object;

import java.io.Serializable;

public interface Targeter extends Serializable {
	SpaceObject getSpaceObject();

	SpaceObject getTarget();

	void setTarget(SpaceObject target);

	boolean isValidTarget(SpaceObject obj);

	default boolean shouldUpdateTarget() {
		SpaceObject t = getTarget();
		return t == null || t.isDestroyed() || !isValidTarget(t);
	}
}
