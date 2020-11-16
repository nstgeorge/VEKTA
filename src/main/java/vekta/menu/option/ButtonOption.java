package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;

import static processing.core.PApplet.max;
import static vekta.Vekta.*;

public abstract class ButtonOption extends MenuOption {

	@Override
	public void render(Menu menu, int index) {
		String name = getName();
		MenuHandle handle = menu.getHandle();
		float x = handle.getItemX();
		float y = handle.getY() + handle.getItemY(index);
		boolean selected = menu.getIndex() == index;

		// Draw border
		v.strokeWeight(selected ? 2 : 1);
		v.stroke(selected ? 255 : menu.getDefault() == this ? 100 : getBorderColor());
		//		v.noFill();
		v.fill(BUTTON_COLOR);
		v.rect(x, y, max(handle.getItemWidth(), v.textWidth(name) + 20) + (selected ? 15 : 0), handle.getItemHeight());
		v.strokeWeight(1);

		// Draw text
		v.fill(isEnabled() ? getColor() : 100);
		v.text(name, x, y - 3);
	}

	public int getBorderColor() {
		return UI_COLOR;
	}
}