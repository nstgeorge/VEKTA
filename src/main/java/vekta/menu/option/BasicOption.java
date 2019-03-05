package vekta.menu.option;

import vekta.menu.Menu;

import java.util.function.Consumer;

public class BasicOption implements MenuOption {
	private final String name;
	private final Consumer<Menu> action;

	public BasicOption(String name, Runnable action) {
		this(name, menu -> action.run());
	}

	public BasicOption(String name, Consumer<Menu> action) {
		this.name = name;
		this.action = action;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void select(Menu menu) {
		action.accept(menu);
	}
}
