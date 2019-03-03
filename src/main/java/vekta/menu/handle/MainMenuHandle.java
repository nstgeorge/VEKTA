package vekta.menu.handle;

import processing.core.PVector;
import vekta.Resources;
import vekta.context.Hyperspace;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;

import static processing.core.PConstants.CENTER;
import static vekta.Vekta.v;

/**
 * Main menu renderer
 */
public class MainMenuHandle extends MenuHandle {
	public static final Hyperspace HYPERSPACE = new Hyperspace(
			new PVector(v.width / 2F, v.height / 2F - 100),
			0.1F,
			170);

	public MainMenuHandle(MenuOption def) {
		super(def);
	}

	@Override
	public void focus(Menu menu) {
		Resources.setMusic("theme");
	}

	@Override
	public void beforeDraw() {
		super.beforeDraw();

		HYPERSPACE.render();
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.shapeMode(CENTER);
		v.shape(Resources.logo, v.width / 2F, v.height / 4F, 339.26F, 100);

		v.textSize(14);
		v.text("Created by Nate St. George", v.width / 2F, (v.height / 2F) + 100 * (menu.size() + 1));
	}
}
