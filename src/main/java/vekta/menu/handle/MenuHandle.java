package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;

import java.io.Serializable;

import static processing.core.PConstants.CENTER;
import static vekta.Vekta.*;

/**
 * Default inject renderer implementation; draws buttons and select text
 */
public class MenuHandle implements Serializable {
	private static final int ITEMS_BEFORE_SCROLL = 5; // Number of items before menu starts scrolling

	// Internal menu reference for item scrolling (evaluating this approach)
	private Menu menu;

	public MenuHandle() {
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
		// Scroll items 
		int y = v.height / 2 - 64 + i * getSpacing();
		int extraOptions = menu.size() - ITEMS_BEFORE_SCROLL;
		if(extraOptions > 0) {
			y -= menu.getIndex() * getSpacing() * extraOptions / menu.size();
		}
		return y;
	}

	public String getSelectVerb() {
		return "select";
	}

	public KeyBinding getShortcutKey() {
		return null;
	}

	public void init(Menu menu) {
	}

	public void focus(Menu menu) {
		this.menu = menu;
	}

	public void beforeDraw() {
		v.clear();
	}

	public void render(Menu menu) {
		beforeDraw();

		v.noStroke();
		v.fill(255);
		v.textFont(BODY_FONT);
		v.textSize(24);
		v.textAlign(CENTER, CENTER);
		v.rectMode(CENTER);
		for(int i = 0; i < menu.size(); i++) {
			MenuOption opt = menu.get(i);
			opt.onUpdate(menu);
			drawButton(menu, opt, i);
		}

		v.noStroke();
		v.fill(255);
		v.textAlign(CENTER);

		if(menu.getCursor().isEnabled()) {
			v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to " + getSelectVerb(), getButtonX(), getButtonY(menu.size()) + 100);
		}
	}

	protected void drawButton(Menu menu, MenuOption opt, int index) {
		float yPos = getButtonY(index);
		boolean selected = menu.getIndex() == index;

		String name = opt.getName();

		// Draw border
		v.stroke(selected ? 255 : opt == menu.getDefault() ? 100 : opt.getBorderColor());
		v.noFill();
		v.rect(getButtonX(), yPos, max(getButtonWidth(), v.textWidth(name) + 20) + (selected ? 10 : 0), 50);

		// Draw text
		v.fill(opt.isEnabled() ? opt.getColor() : 100);
		v.text(name, getButtonX(), yPos - 3);
	}

	public void keyPressed(Menu menu, KeyBinding key) {
		if(key == KeyBinding.MENU_CLOSE || key == getShortcutKey()) {
			menu.close();
		}
		else if(key == KeyBinding.MENU_UP) {
			Resources.playSound("change");
			menu.scroll(-1);
		}
		else if(key == KeyBinding.MENU_DOWN) {
			Resources.playSound("change");
			menu.scroll(1);
		}
		else if(key == KeyBinding.MENU_SELECT) {
			if(menu.getCursor().isEnabled()) {
				Resources.playSound("select");
				menu.selectCursor();
			}
		}
	}
}