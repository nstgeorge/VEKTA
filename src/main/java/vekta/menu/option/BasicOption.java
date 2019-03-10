package vekta.menu.option;

import vekta.menu.Menu;

import java.io.Serializable;

public class BasicOption implements MenuOption {
	private final String name;
	private final MenuAction action;
	private int color;
	private boolean remove;

	public BasicOption(String name, Runnable action) {
		this(name, menu -> action.run());
	}

	public BasicOption(String name, MenuAction action) {
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

	public BasicOption withRemoval() {
		this.remove = true;
		return this;
	}

	@Override
	public void select(Menu menu) {
		action.select(menu);
		if(remove) {
			menu.remove(this);
		}
	}

	public interface MenuAction extends Serializable {
		void select(Menu menu);
	}
}
