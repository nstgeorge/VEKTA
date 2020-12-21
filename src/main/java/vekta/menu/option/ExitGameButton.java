package vekta.menu.option;

import vekta.menu.Menu;

import static vekta.Vekta.v;

public class ExitGameButton extends ButtonOption {
	private final String name;

	public ExitGameButton(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onSelect(Menu menu) {
		v.exit();
	}
}
