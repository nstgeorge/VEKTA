package vekta.menu.option;

import vekta.Vekta;
import vekta.menu.Menu;

public interface MenuOption {
	String getName();
	
	default int getColor() {
		return Vekta.UI_COLOR;
	}

	void select(Menu menu);
}