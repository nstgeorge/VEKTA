package vekta.menu.handle;

import vekta.KeyCategory;

import static vekta.Vekta.v;

/**
 * Menu renderer for game settings
 */
public class SettingsMenuHandle extends MenuHandle {
	private final KeyCategory category;

	public SettingsMenuHandle() {
		this(null);
	}

	public SettingsMenuHandle(KeyCategory category) {
		this.category = category;
	}

	public KeyCategory getCategory() {
		return category;
	}

	@Override
	public int getItemY(int i) {
		// Move everything upwards a bit
		return super.getItemY(i - 1);
	}

	@Override
	public int getItemWidth() {
		return super.getItemWidth() * 2;
	}

	@Override
	public int getSpacing() {
		return getItemHeight() + 20;
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
		v.text(getCategory() != null ? getCategory().getTitle() : "Settings", getItemX(), getItemY(-2));
	}
}
