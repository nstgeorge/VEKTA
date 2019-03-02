package vekta.object.module;

import vekta.object.Ship;

public interface Module {
	// Define per-second energy consumption rate
	float PER_SECOND = 1 / 60F;
	float PER_MINUTE = PER_SECOND / 60F;
	
	String getName();
	
	ModuleType getType();
	
	boolean isBetter(Module other);

	default void onInstall(Ship ship) {}

	default void onUninstall(Ship ship) {}
	
	default void onUpdate(Ship ship) {}
	
	default void onKeyPress(Ship ship, char key) {}
}
