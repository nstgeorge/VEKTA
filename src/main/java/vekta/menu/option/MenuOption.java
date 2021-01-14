package vekta.menu.option;

import processing.event.KeyEvent;
import vekta.KeyBinding;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.UI_COLOR;

public abstract class MenuOption implements Serializable {

	public abstract String getName();

	public abstract void render(Menu menu, int index);

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

	public float getHeight() {
		//return menu.getHandle().getItemY(menu.getOptions().indexOf(this));
		return 50;
	}
}