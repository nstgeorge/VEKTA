package vekta.object.module;

import vekta.menu.Menu;
import vekta.object.ModularShip;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

public interface Module {
	// Define per-second energy consumption rate
	float PER_SECOND = 1 / 60F;
	float PER_MINUTE = PER_SECOND / 60F;

	String getName();

	ModuleType getType();

	boolean isBetter(Module other);

	Module getVariant();

	default void onInstall(ModularShip ship) {
	}

	default void onUninstall(ModularShip ship) {
	}

	default void onUpdate() {
	}

	default void onDepart(SpaceObject s) {
	}

	default void onKeyPress(char key) {
	}

	default void onKeyRelease(char key) {
	}

	default void onLandingMenu(LandingSite site, Menu menu) {
	}
}
