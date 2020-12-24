package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.menu.option.InstallModuleButton;
import vekta.menu.option.MenuOption;
import vekta.module.Module;
import vekta.module.ModuleType;

import java.util.List;

import static vekta.Vekta.v;

/**
 * Module loadout menu renderer
 */
public class LoadoutMenuHandle extends MenuHandle {
	private final List<Module> modules;
	private final boolean replace;

	public LoadoutMenuHandle(List<Module> modules, boolean replace) {
		super(0, 200, v.width, v.height - 300);
		this.modules = modules;
		this.replace = replace;
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

//	@Override
//	public int getItemY(int i) {
//		return super.getItemY(i - 1);
//	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_LOADOUT;
	}

	@Override
	public void render() {
		super.render();

		v.textSize(32);
		v.fill(v.color(100));
		v.textAlign(v.CENTER);
		v.text("Available:", getItemX(), getItemY(-1));
		v.textAlign(v.LEFT);
		v.text("Installed:", v.width - getItemX() - 20, getLoadoutOffset(-2));

		ModuleType type = null;
		if(replace) {
			MenuOption item = getMenu().getCursor();
			if(item instanceof InstallModuleButton) {
				type = ((InstallModuleButton)item).getModule().getType();
			}
		}

		v.textSize(24);
		for(int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);
			boolean relevant = m.getType() == type;
			v.fill(relevant ? 100 : 200);
			v.text(m.getName(), v.width - getItemX() + (relevant ? -10 : 0), getLoadoutOffset(i));
		}
	}

	private float getLoadoutOffset(int position) {
		return (v.width / 4F + getItemY(position)) / 2;
	}
}
