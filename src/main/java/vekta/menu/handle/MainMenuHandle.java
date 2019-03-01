package vekta.menu.handle;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;

import static processing.core.PConstants.CENTER;

/**
 * Menu renderer for landing on planets
 */
public class MainMenuHandle extends MenuHandle {

	public MainMenuHandle(MenuOption def) {
		super(def);
	}

	public void render(Menu menu) {
		super.render(menu);

		v.shapeMode(CENTER);
		v.shape(Resources.logo, v.width / 2F, v.height / 4F, 339.26F, 100);

		v.textSize(14);
		v.text("Created by Nate St. George", v.width / 2F, (v.height / 2F) + 100 * (menu.size() + 1));
	}
}
