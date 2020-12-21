package vekta.module;

import vekta.KeyBinding;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

import java.io.Serializable;

public interface Module extends Serializable {
	// Define per-second energy consumption rate
	float PER_SECOND = 1 / 60F;
	float PER_MINUTE = PER_SECOND / 60F;

	String getName();

	ModuleType getType();

	int getMass();

	float getValueScale();

	default boolean isBetter(Module other) {
		return getValueScale() > other.getValueScale();
	}

	Module createVariant();

	boolean isApplicable(ModularShip ship);

	default void onInstall(ModularShip ship) {
	}

	default void onUninstall(ModularShip ship) {
	}

	default void onDamageShip(ModularShip.DamageAttempt attempt) {
	}

	default void onUpdate() {
	}

	default void onAnalogKeyPress(float value) {
	}

	default void onKeyPress(KeyBinding key) {
	}

	default void onKeyRelease(KeyBinding key) {
	}

	default void onItemMenu(Item item, Menu menu) {
	}

	default void onMenu(Menu menu) {
	}

	default void onInfo(InfoGroup info) {
	}
}
