package vekta.object.module;

import vekta.object.ControllableShip;
import vekta.object.SpaceObject;

public interface Module {
	// Define per-second energy consumption rate
	float PER_SECOND = 1 / 60F;
	float PER_MINUTE = PER_SECOND / 60F;

	String getName();

	ModuleType getType();

	boolean isBetter(Module other);

	default void onInstall(ControllableShip ship) {
	}

	default void onUninstall(ControllableShip ship) {
	}

	default void onUpdate() {
	}

	default void onDepart(SpaceObject s) {
	}

	default void onKeyPress(char key) {
	}

	default void onKeyRelease(char key) {
	}
}
