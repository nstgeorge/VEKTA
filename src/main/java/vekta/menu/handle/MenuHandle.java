package vekta.menu.handle;

import vekta.Resources;
import vekta.context.Context;
import vekta.menu.Menu;
import vekta.menu.option.BackOption;
import vekta.menu.option.MenuOption;

import static processing.core.PConstants.*;
import static vekta.Vekta.*;

/**
 * Default menu renderer implementation; draws buttons and select text
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

	public String getHelperText() {
		return "X to select";
	}

	public void focus(Menu menu) {
	}

	public void beforeDraw() {
		v.clear(); // TODO: only clear region behind menu
		v.camera();
		v.noLights();
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
			drawButton(menu.get(i), i, menu.getIndex() == i);
		}

		v.noStroke();
		v.fill(255);
		v.textAlign(CENTER);
		v.text(getHelperText(), getButtonX(), getButtonY(menu.size()) + 100);
	}

	void drawButton(MenuOption opt, int index, boolean selected) {
		float yPos = getButtonY(index);
		
		// Draw border
		v.stroke(selected ? 255 : UI_COLOR);
		v.noFill();
		v.rect(getButtonX(), yPos, getButtonWidth() + (selected ? 10 : 0), 50);
		
		// Draw text
		v.fill(opt.getColor());
		v.noStroke();
		v.text(opt.getName(), getButtonX(), yPos - 3);
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
			Resources.playSound("select");
			menu.getCursor().select(menu);
		}
	}
}