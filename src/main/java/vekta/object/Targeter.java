package vekta.object;

public interface Targeter {
	SpaceObject getTarget();

	void setTarget(SpaceObject target);

	boolean isValidTarget(SpaceObject obj);
}
