package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.ChoicesInputController;
import vekta.menu.option.input.FloatSettingWatcher;
import vekta.menu.option.input.InputController;
import vekta.menu.option.input.InputOption;

import java.util.Arrays;

import static vekta.Vekta.setContext;

public class SettingsMenuButton implements ButtonOption {

	@Override
	public String getName() {
		return "Settings";
	}

	@Override
	public void onSelect(Menu menu) {
		InputController<Float> audioCtrl = new ChoicesInputController<>(Arrays.asList(0F, 1F), f -> f == 0 ? "Off" : "On");

		Menu sub = new Menu(menu, new SettingsMenuHandle());
		sub.add(new InputOption<>("Sound", new FloatSettingWatcher("sound"), audioCtrl));
		sub.add(new InputOption<>("Music", new FloatSettingWatcher("music"), audioCtrl));
		sub.add(new KeyBindingMenuButton());
		sub.addDefault();
		setContext(sub);
	}
}
