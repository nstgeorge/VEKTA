package vekta.object;

public interface Targeter {
	SpaceObject getTarget();

	void setTarget(SpaceObject target);

	boolean isValidTarget(SpaceObject obj);
	
	default boolean shouldResetTarget() {
		SpaceObject t = getTarget();
		return t == null || !isValidTarget(t);
	}
}
