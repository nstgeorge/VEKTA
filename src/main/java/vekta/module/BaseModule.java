package vekta.module;

import vekta.KeyBinding;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

import java.io.Serializable;

public interface BaseModule extends Serializable {
	// Define per-second energy consumption rate
	float PER_SECOND = 1 / 60F;
	float PER_MINUTE = PER_SECOND / 60F;

	String getName();

	ModuleType getType();

	int getMass();

	float getValueScale();

	default boolean isBetter(BaseModule other) {
		return getValueScale() > other.getValueScale();
	}

	BaseModule createVariant();

	boolean isApplicable(ModularShip ship);

	default void onInstall(ModularShip ship) {
	}

	default void onUninstall(ModularShip ship) {
	}

	default void onDamageShip(ModularShip.DamageAttempt attempt) {
	}

	default void onUpdate() {
	}

	default void onKeyPress(KeyBinding key) {
	}

	default void onKeyRelease(KeyBinding key) {
	}

	default void onAnalogKeyPress(float value) {
	}

	default void onControlStickMoved(float x, float y, int side) {
	}

	default void onItemMenu(Item item, Menu menu) {
	}

	default void onMenu(Menu menu) {
	}

	default void onInfo(InfoGroup info) {
	}
}
