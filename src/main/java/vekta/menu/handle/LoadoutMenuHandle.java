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
	public int getButtonWidth() {
		return v.width / 3;
	}

	@Override
	public int getButtonX() {
		return v.width / 6 + getButtonWidth() / 2;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i - 1);
	}

	@Override
	public String getSelectVerb() {
		return "install";
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
		v.text("Available:", getButtonX(), getButtonY(-1));
		v.textAlign(v.LEFT);
		v.text("Installed:", v.width - getButtonX() - 20, getLoadoutOffset(-2));

		v.textSize(24);
		v.fill(v.color(200));
		for(int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);
			v.text(m.getName(), v.width - getButtonX(), getLoadoutOffset(i));
		}
	}

	private float getLoadoutOffset(int position) {
		return (v.width / 4F + getButtonY(position)) / 2;
	}
}
