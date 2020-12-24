package vekta.menu.handle;

import processing.event.KeyEvent;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.ui.container.ScrollableContainer;

import java.io.Serializable;

import static processing.core.PConstants.CENTER;
import static vekta.Vekta.BODY_FONT;
import static vekta.Vekta.v;

/**
 * Default menu renderer implementation; draws buttons and selection text
 */
public class MenuHandle extends ScrollableContainer implements Serializable {

	private Menu menu;

	public MenuHandle() {
		this(0, v.height / 2 - 128, v.width, v.height / 2 + 128);
	}

	public MenuHandle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public int getSpacing() {
		return 50;
	}

	public int getItemHeight() { return 50; }

	public int getItemWidth() {
		return 200;
	}

	public int getItemX() {
		return v.width / 2;
	}

	public int getItemY(int i) {
		return (i + 1) * (getSpacing() + getItemHeight());
	}

	public KeyBinding getShortcutKey() {
		return null;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public void init(Menu menu) {
		setMenu(menu);
	}

	public void focus(Menu menu) {
	}

	public void unfocus(Menu menu) {
	}

	public void beforeDraw() {
		v.clear();
	}

	protected boolean shouldDrawBlockingElements() {
		return false;
	}

	@Override
	public void render() {
//		float targetOffset = extraOptions > 0 ? menu.getIndex() * getSpacing() * extraOptions / (float)menu.size() : 0;
//		scrollOffset += (targetOffset - scrollOffset) * SCROLL_RATE / 60;

		beforeDraw();

		if(menu != null) {

			v.noStroke();
			v.fill(255);
			v.textFont(BODY_FONT);
			v.textSize(24);
			v.textAlign(CENTER, CENTER);
			v.rectMode(CENTER);

			beginScrolledContext();

			for(int i = 0; i < menu.size(); i++) {
				MenuOption opt = menu.get(i);
				opt.onUpdate(menu);
				drawButton(opt, i);
			}

			endScrolledContext();

			if(shouldDrawBlockingElements()) {
				drawBlockingElements();
			}

			v.noStroke();
			v.fill(255);
			v.textAlign(CENTER);

			String selectVerb = menu.getCursor().getSelectVerb();
			if(menu.getCursor().isEnabled() && selectVerb != null) {
				v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to " + selectVerb, getItemX(), getItemY(menu.size()) + getY() + 100);
			}
		}
	}

	protected void drawButton(MenuOption opt, int index) {
		opt.render(menu, index);
	}

	public void keyPressed(KeyEvent event) {
		menu.getCursor().keyPressed(menu, event);
	}

	public void keyPressed(KeyBinding key) {
		if(menu.getCursor().interceptKeyPressed(menu, key)) {
			return;
		}
		else if(key == KeyBinding.MENU_CLOSE || key == getShortcutKey()) {
			menu.close();
			return;
		}

		if(key == KeyBinding.MENU_UP) {
			scroll(-1);
			Resources.playSound("change");
		}
		else if(key == KeyBinding.MENU_DOWN) {
			scroll(1);
			Resources.playSound("change");
		}
		else if(key == KeyBinding.MENU_SELECT) {
			if(menu.getCursor().isEnabled()) {
				menu.selectCursor();
				Resources.playSound("select");
			}
		}
	}

	public void onChange() { }

	@Override
	public void onScroll(int amount) {
		menu.scroll(amount);
	}
}