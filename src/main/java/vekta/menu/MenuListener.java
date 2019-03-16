package vekta.menu;

import vekta.menu.option.MenuOption;

public interface MenuListener {
	default void onFocus() {
	}
	
	default void onHover(MenuOption option) {
	}

	default void onSelect(MenuOption option) {
	}
}
