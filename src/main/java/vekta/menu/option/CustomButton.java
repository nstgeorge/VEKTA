package vekta.menu.option;

import vekta.context.Context;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.setContext;

public class CustomButton implements ButtonOption {
	private final String name;
	private final MenuAction action;
	private int color;
	private boolean remove;

	public CustomButton(String name, Context context) {
		this(name, menu -> setContext(context));
	}

	public CustomButton(String name, MenuAction action) {
		this.name = name;
		this.action = action;
		this.color = ButtonOption.super.getColor();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor() {
		return color;
	}

	public CustomButton withColor(int color) {
		this.color = color;
		return this;
	}

	public CustomButton withRemoval() {
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
