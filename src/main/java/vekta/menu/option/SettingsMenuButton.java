package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.*;

import static vekta.Vekta.setContext;

public class SettingsMenuButton implements ButtonOption {

	@Override
	public String getName() {
		return "Settings";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new SettingsMenuHandle());

		sub.add(new InputOption<>("Sound",
				new FloatSettingWatcher("sound"),
				new VolumeInputController(0, 1, .1F, new BooleanSettingWatcher("muteSound"))));

		sub.add(new InputOption<>("Music",
				new FloatSettingWatcher("music"),
				new VolumeInputController(0, 1, .1F, new BooleanSettingWatcher("muteMusic"))));

		sub.add(new InputOption<>("Scroll speed",
				new FloatSettingWatcher("zoomSpeed"),
				new FloatRangeInputController(0.1F, 10, .1F)));
		
		sub.add(new KeyBindingMenuButton());
		sub.addDefault();
		setContext(sub);
	}
}
