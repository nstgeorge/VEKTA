package vekta.menu.option;

import processing.event.KeyEvent;
import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.ui.container.ContainerMember;

import java.io.Serializable;

import static vekta.Vekta.UI_COLOR;

public abstract class MenuOption implements Serializable, ContainerMember {

	private Menu menu;

	public final void init(Menu menu) {
		this.menu = menu;
	}

	public abstract String getName();

	public abstract void render(Menu menu, int index);

	@Override
	public void render() {
		render(menu, menu.getOptions().indexOf(this));
	}

	public int getColor() {
		return UI_COLOR;
	}

	public String getSelectVerb() {
		return "select";
	}

	public boolean isEnabled() {
		return true;
	}

	public void onUpdate(Menu menu) {
	}

	public abstract void onSelect(Menu menu);

	public boolean interceptKeyPressed(Menu menu, KeyBinding key) {
		return false;
	}

	public void keyPressed(Menu menu, KeyEvent event) {
	}

	@Override
	public float getSizeY() {
		//return menu.getHandle().getItemY(menu.getOptions().indexOf(this));
		return 50;
	}

	@Override
	public float getYPadding() {
		return menu.getHandle().getSpacing();
	}
}