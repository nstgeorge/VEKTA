package vekta.menu.option;

import vekta.KeyBinding;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.UI_COLOR;

public interface MenuOption extends Serializable {
	String getName();

	void draw(Menu menu, int index);

	default int getColor() {
		return UI_COLOR;
	}

	default String getSelectVerb() {
		return "select";
	}

	default boolean isEnabled() {
		return true;
	}

	default void onUpdate(Menu menu) {
	}

	void onSelect(Menu menu);

	default boolean interceptKeyPressed(Menu menu, KeyBinding key) {
		return false;
	}
}