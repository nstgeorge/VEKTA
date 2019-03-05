package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.module.Module;

import java.util.List;

import static vekta.Vekta.v;

/**
 * Module loadout inject renderer
 */
public class LoadoutMenuHandle extends MenuHandle {
	private final List<Module> modules;

	public LoadoutMenuHandle(MenuOption defaultOption, List<Module> modules) {
		super(defaultOption);

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
	public String getHelperText() {
		return "X to install";
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(v.color(100));
		v.textAlign(v.CENTER);
		v.text("Available:", getButtonX(), getButtonY(-2));
		v.textAlign(v.LEFT);
		v.text("Installed:", v.width - getButtonX() - 20, getButtonY(-2));

		v.textSize(24);
		v.fill(v.color(200));
		for(int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);

			v.text(m.getName(), v.width - getButtonX(), getButtonY(i));
		}
	}
}
