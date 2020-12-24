package vekta.menu.handle;

import static vekta.Vekta.v;

/**
 * Renderer for keybind menus
 */
public class KeybindMenuHandle extends MenuHandle {

	String name;

	public KeybindMenuHandle(String name) {
		this(name, 0, 300, v.width, v.height - 400);
	}

	public KeybindMenuHandle(String name, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.name = name;
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
	}

	@Override
	public void render() {
		super.render();

		v.textSize(32);
		v.fill(255);
		v.text(name, getItemX(), getY() - (getY() / 2));
	}
}
