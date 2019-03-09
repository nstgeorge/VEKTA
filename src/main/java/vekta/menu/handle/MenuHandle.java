package vekta.menu.handle;

import vekta.ControlKey;
import vekta.Resources;
import vekta.Settings;
import vekta.context.Context;
import vekta.menu.Menu;
import vekta.menu.option.BackOption;
import vekta.menu.option.MenuOption;

import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.DISABLE_DEPTH_TEST;
import static vekta.Vekta.*;

/**
 * Default inject renderer implementation; draws buttons and select text
 */
public class MenuHandle {
	private final MenuOption defaultOption;

	public MenuHandle(Context parent) {
		this(new BackOption(parent));
	}

	public MenuHandle(MenuOption defaultOption) {
		this.defaultOption = defaultOption;
	}

	public MenuOption getDefault() {
		return defaultOption;
	}

	public int getSpacing() {
		return 100;
	}

	public int getButtonWidth() {
		return 200;
	}

	public int getButtonX() {
		return v.width / 2;
	}

	public int getButtonY(int i) {
		return v.height / 2 - 64 + i * getSpacing();
	}

	public String getSelectVerb() {
		return "select";
	}

	public void init(Menu menu) {
	}

	public void focus(Menu menu) {
	}

	public void beforeDraw() {
		v.clear();
		//		v.camera();
		//		v.noLights();
		v.hint(DISABLE_DEPTH_TEST);
	}

	public void render(Menu menu) {
		beforeDraw();

		v.noStroke();
		v.fill(255);
		v.textFont(bodyFont);
		v.textSize(24);
		v.textAlign(CENTER, CENTER);
		v.rectMode(CENTER);
		for(int i = 0; i < menu.size(); i++) {
			drawButton(menu, menu.get(i), i);
		}

		v.noStroke();
		v.fill(255);
		v.textAlign(CENTER);

		if(menu.getCursor().isEnabled()) {
			v.text(Settings.getControlString(ControlKey.MENU_SELECT) + " to " + getSelectVerb(), getButtonX(), getButtonY(menu.size()) + 100);
		}
	}

	void drawButton(Menu menu, MenuOption opt, int index) {
		float yPos = getButtonY(index);
		boolean selected = menu.getIndex() == index;

		// Draw border
		v.stroke(selected ? 220 : UI_COLOR);
		v.noFill();
		v.rect(getButtonX(), yPos, getButtonWidth() + (selected ? 10 : 0), 50);

		// Draw text
		v.fill(opt.isEnabled() ? opt.getColor() : 100);
		v.noStroke();
		v.text(opt.getName(), getButtonX(), yPos - 3);
	}

	public void keyPressed(Menu menu, ControlKey key) {
		if(key == ControlKey.MENU_CLOSE) {
			menu.close();
		}
		else if(key == ControlKey.MENU_UP) {
			Resources.playSound("change");
			menu.scroll(-1);
		}
		else if(key == ControlKey.MENU_DOWN) {
			Resources.playSound("change");
			menu.scroll(1);
		}
		else if(key == ControlKey.MENU_SELECT) {
			if(menu.getCursor().isEnabled()) {
				Resources.playSound("select");
				menu.select();
			}
		}
	}
}