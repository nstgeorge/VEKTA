package vekta.menu.option;

import vekta.menu.Menu;

import java.util.function.Consumer;

public class BasicOption implements MenuOption {
	private final String name;
	private final Consumer<Menu> action;
	private int color;

	public BasicOption(String name, Runnable action) {
		this(name, menu -> action.run());
	}

	public BasicOption(String name, Consumer<Menu> action) {
		this.name = name;
		this.action = action;
		this.color = MenuOption.super.getColor();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor() {
		return color;
	}

	public BasicOption withColor(int color) {
		this.color = color;
		return this;
	}

	@Override
	public void select(Menu menu) {
		action.accept(menu);
	}
}
