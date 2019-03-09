package vekta.module;

import vekta.ControlKey;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

public interface Module {
	// Define per-second energy consumption rate
	float PER_SECOND = 1 / 60F;
	float PER_MINUTE = PER_SECOND / 60F;

	String getName();

	ModuleType getType();

	boolean isBetter(Module other);

	Module getVariant();

	boolean isApplicable(ModularShip ship);

	default void onInstall(ModularShip ship) {
	}

	default void onUninstall(ModularShip ship) {
	}

	default void onUpdate() {
	}

	default void onKeyPress(ControlKey key) {
	}

	default void onKeyRelease(ControlKey key) {
	}
	
	default void onItemMenu(Item item, Menu menu) {
	}

	default void onItemMenu(Menu menu) {
	}
}
