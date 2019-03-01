package vekta.menu.handle;

import vekta.context.Context;
import vekta.Resources;
import vekta.Vekta;
import vekta.menu.BackOption;
import vekta.menu.Menu;
import vekta.menu.MenuOption;

import static processing.core.PConstants.*;
import static vekta.Vekta.*;

/**
 * Default menu renderer implementation; draws buttons and select text
 */
public class MenuHandle {
	// Default parameters
	private static final int DEF_SPACING = 100;
	private static final int DEF_WIDTH = 200;

	private final MenuOption defaultOption;
	private final int spacing;
	private final int buttonWidth;

	public MenuHandle(Context parent) {
		this(new BackOption(parent));
	}

	public MenuHandle(MenuOption defaultOption) {
		this(defaultOption, DEF_SPACING, DEF_WIDTH);
	}

	public MenuHandle(MenuOption defaultOption, int spacing, int buttonWidth) {
		this.defaultOption = defaultOption;
		this.spacing = spacing;
		this.buttonWidth = buttonWidth;
	}

	public MenuOption getDefault() {
		return defaultOption;
	}

	public void render(Menu menu) {
		Vekta v = getInstance();
		v.clear(); // TODO: find a way to avoid clearing without creating weird artifacts
		v.hint(DISABLE_DEPTH_TEST);
		v.camera();
		v.noLights();

		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER, CENTER);
		v.textFont(bodyFont);
		v.textSize(24);
		for(int i = 0; i < menu.size(); i++) {
			drawButton(menu.get(i), (v.height / 2) + (i * spacing), menu.getIndex() == i);
		}

		//textFont(bodyFont);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.text("X to select", v.width / 2F, (v.height / 2F) + (menu.size() * 100) + 200);
	}

	void drawButton(MenuOption opt, int yPos, boolean selected) {
		Vekta v = getInstance();
		if(selected)
			v.stroke(255);
		else
			v.stroke(UI_COLOR);
		v.fill(1);
		v.rectMode(CENTER);
		v.rect(v.width / 2F, yPos, buttonWidth + (selected ? 10 : 0), 50);
		// Text ----------------------
		//textFont(bodyFont);
		v.stroke(0);
		v.fill(getButtonColor(opt));
		v.text(opt.getName(), v.width / 2F, yPos - 3);
	}

	int getButtonColor(MenuOption opt) {
		return UI_COLOR;
	}

	public void keyPressed(Menu menu, char key) {
		if(key == ESC) {
			if(defaultOption != null) {
				defaultOption.select(menu);
			}
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