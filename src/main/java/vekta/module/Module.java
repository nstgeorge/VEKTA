package vekta.module;

import vekta.KeyBinding;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

import java.io.Serializable;

public interface Module extends Serializable {
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

	default void onKeyPress(KeyBinding key) {
	}

	default void onKeyRelease(KeyBinding key) {
	}
	
	default void onItemMenu(Item item, Menu menu) {
	}

	default void onItemMenu(Menu menu) {
	}
}
