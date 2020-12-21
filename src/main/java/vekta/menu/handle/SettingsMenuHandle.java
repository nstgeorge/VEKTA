package vekta.menu.handle;

import vekta.menu.Menu;

import static vekta.Vekta.v;

/**
 * Menu renderer for game settings
 */
public class SettingsMenuHandle extends MenuHandle {

	public SettingsMenuHandle() {
		this(0, 300, v.width, v.height - 400);
	}

	public SettingsMenuHandle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public int getItemWidth() {
		return super.getItemWidth() * 2;
	}

	@Override
	public int getSpacing() {
		return 20;
	}

	@Override
	public void beforeDraw() {
		super.beforeDraw();

		//		MainMenuHandle.HYPERSPACE.render();
	}

	@Override
	public void render() {
		super.render();

		v.textSize(32);
		v.fill(255);
		v.text("Settings", getItemX(), getY() - (getY() / 2));
	}
}
