package vekta.menu.handle;

import vekta.menu.Menu;

import static vekta.Vekta.v;

/**
 * Menu renderer for game settings
 */
public class SettingsMenuHandle extends MenuHandle {
	@Override
	public int getItemWidth() {
		return super.getItemWidth() * 2;
	}

	@Override
	public int getSpacing() {
		return 60;
	}

	@Override
	public int getItemY(int i) {
		return super.getItemY(i - 3);
	}

	@Override
	public void beforeDraw() {
		super.beforeDraw();

				MainMenuHandle.HYPERSPACE.render();
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(255);
		v.text("Settings", getItemX(), getItemY(-2));
	}
}
