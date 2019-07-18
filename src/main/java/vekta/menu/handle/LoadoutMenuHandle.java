package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.module.Module;

import java.util.List;

import static vekta.Vekta.v;

/**
 * Module loadout menu renderer
 */
public class LoadoutMenuHandle extends MenuHandle {
	private final List<Module> modules;

	public LoadoutMenuHandle(List<Module> modules) {
		this.modules = modules;
	}

	@Override
	public int getSpacing() {
		return 70;
	}

	@Override
	public int getItemWidth() {
		return v.width / 3;
	}

	@Override
	public int getItemX() {
		return v.width / 6 + getItemWidth() / 2;
	}

	@Override
	public int getItemY(int i) {
		return super.getItemY(i - 1);
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_LOADOUT;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(v.color(100));
		v.textAlign(v.CENTER);
		v.text("Available:", getItemX(), getItemY(-1));
		v.textAlign(v.LEFT);
		v.text("Installed:", v.width - getItemX() - 20, getLoadoutOffset(-2));

		v.textSize(24);
		v.fill(v.color(200));
		for(int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);
			v.text(m.getName(), v.width - getItemX(), getLoadoutOffset(i));
		}
	}

	private float getLoadoutOffset(int position) {
		return (v.width / 4F + getItemY(position)) / 2;
	}
}
