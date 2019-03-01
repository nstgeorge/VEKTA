package vekta.menu.option;

import vekta.menu.Menu;

public interface MenuOption {
	String getName();

	void select(Menu menu);
}