package vekta.menu.option;

import vekta.menu.Menu;

import static vekta.Vekta.v;

public class ExitGameOption implements MenuOption {
	private final String name;

	public ExitGameOption(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void select(Menu menu) {
		v.exit();
	}
}
