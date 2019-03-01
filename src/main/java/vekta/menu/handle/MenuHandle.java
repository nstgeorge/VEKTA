package vekta.menu.handle;

import vekta.Resources;
import vekta.Vekta;
import vekta.context.Context;
import vekta.menu.option.BackOption;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.menu.option.TradeOption;

import static processing.core.PConstants.*;
import static vekta.Vekta.*;

/**
 * Default menu renderer implementation; draws buttons and select text
 */
public class MenuHandle {
	protected static final Vekta v = getInstance();

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

	public void render(Menu menu) {
		v.clear();
		v.hint(DISABLE_DEPTH_TEST);
		v.camera();
		v.noLights();

		// TODO
		//		// Partially fill in background
		//		v.fill(v.color(100));
		//		v.rect((v.width - buttonWidth) / 2F, (v.width + buttonWidth) / 2F, 0, v.height);

		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER, CENTER);
		v.textFont(bodyFont);
		v.textSize(24);
		for(int i = 0; i < menu.size(); i++) {
			drawButton(menu.get(i), (v.height / 2) + (i * getSpacing()), menu.getIndex() == i);
		}

		//textFont(bodyFont);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.text("X to select", v.width / 2F, (v.height / 2F) + (menu.size() * 100) + 200);
	}

	void drawButton(MenuOption opt, int yPos, boolean selected) {
		if(selected)
			v.stroke(255);
		else
			v.stroke(UI_COLOR);
		v.fill(1);
		v.rectMode(CENTER);
		v.rect(v.width / 2F, yPos, getButtonWidth() + (selected ? 10 : 0), 50);
		// Text ----------------------
		//textFont(bodyFont);
		v.stroke(0);
		v.fill(getButtonColor(opt));
		v.text(opt.getName(), v.width / 2F, yPos - 3);
	}

	private int getButtonColor(MenuOption opt) {
		// TODO: define special cases in their own classes
		if(opt instanceof TradeOption) {
			return ((TradeOption)opt).getItem().getType().getColor();
		}
		return UI_COLOR;
	}

	public void keyPressed(Menu menu, char key) {
		if(key == ESC) {
			menu.close();
		}
		else if(key == 'w') {
			Resources.playSound("change");
			menu.scroll(-1);
		}
		else if(key == 's') {
			Resources.playSound("change");
			menu.scroll(1);
		}
		else if(key == 'x') {
			Resources.playSound("change");
			menu.getCursor().select(menu);
		}
	}
}