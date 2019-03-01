package vekta.menu;

import vekta.context.Context;

import static vekta.Vekta.setContext;

public class BackOption implements MenuOption {
	private final Context parent;

	public BackOption(Context parent) {
		this.parent = parent;
	}

	@Override
	public String getName() {
		return "Back";
	}

	@Override
	public void select(Menu menu) {
		setContext(parent);
	}
}
