package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.object.module.Module;
import vekta.object.module.Upgradeable;

import java.util.List;

import static vekta.Vekta.v;

/**
 * Module loadout menu renderer
 */
public class LoadoutMenuHandle extends MenuHandle {
	private final Upgradeable upgradeable;

	public LoadoutMenuHandle(MenuOption defaultOption, Upgradeable upgradeable) {
		super(defaultOption);

		this.upgradeable = upgradeable;
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
		v.text("Available Modules:", getButtonX(), getButtonY(-2));
		v.textAlign(v.LEFT);
		v.text("Installed Modules:", v.width - getButtonX() - 20, getButtonY(-2));

		v.textSize(24);
		v.stroke(0);
		v.fill(v.color(200));
		List<Module> modules = upgradeable.getModules();
		for(int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);

			v.text(m.getName(), v.width - getButtonX(), getButtonY(i));
		}
	}
}
