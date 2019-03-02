package vekta.object.module;

import vekta.object.Ship;

public interface Module {
	String getName();
	
	ModuleType getType();
	
	// TODO: show in UI
	boolean isBetter(Module other);

	default void onInstall(Ship ship) {}

	default void onUninstall(Ship ship) {}
	
	default void onUpdate(Ship ship) {}
	
	default void onKeyPress(Ship ship, char key) {}
}
