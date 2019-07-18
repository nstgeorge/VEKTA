package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;

import static processing.core.PApplet.max;
import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

public interface ButtonOption extends MenuOption {
	@Override
	default void draw(Menu menu, int index) {
		String name = getName();
		MenuHandle handle = menu.getHandle();
		float x = handle.getItemX();
		float y = handle.getItemY(index);
		boolean selected = menu.getIndex() == index;

		// Draw border
		v.stroke(selected ? 255 : menu.getDefault() == this ? 100 : getBorderColor());
		v.noFill();
		v.rect(x, y, max(handle.getItemWidth(), v.textWidth(name) + 20) + (selected ? 10 : 0), 50);

		// Draw text
		v.fill(isEnabled() ? getColor() : 100);
		v.text(name, x, y - 3);
	}
	
	default int getBorderColor() {
		return UI_COLOR;
	}
}