package vekta.menu.option;

import vekta.menu.Menu;

import static vekta.Vekta.UI_COLOR;

public interface MenuOption {
	String getName();

	default int getColor() {
		return UI_COLOR;
	}

	default boolean isEnabled() {
		return true;
	}

	void select(Menu menu);
}