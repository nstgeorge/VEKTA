package vekta.menu.option;

import vekta.context.Context;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.setContext;

public class CustomOption implements MenuOption {
	private final String name;
	private final MenuAction action;
	private int color;
	private boolean remove;

	public CustomOption(String name, Context context) {
		this(name, menu -> setContext(context));
	}

	public CustomOption(String name, MenuAction action) {
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

	public CustomOption withColor(int color) {
		this.color = color;
		return this;
	}

	public CustomOption withRemoval() {
		this.remove = true;
		return this;
	}

	@Override
	public void onSelect(Menu menu) {
		action.select(menu);
		if(remove) {
			menu.remove(this);
		}
	}

	public interface MenuAction extends Serializable {
		void select(Menu menu);
	}
}
