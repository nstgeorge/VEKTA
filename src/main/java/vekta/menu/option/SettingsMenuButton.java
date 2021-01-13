package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.*;

import java.util.Arrays;

import static vekta.Vekta.*;

public class SettingsMenuButton extends ButtonOption {

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
				new FloatRangeInputController(.1F, 10, .1F)));

		sub.add(new InputOption<>("Zoom near planets",
				new BooleanSettingWatcher("zoomNearPlanets"),
				new YesNoInputController()));

		if(DEVICE != null && DEVICE.isConnected()) {
			sub.add(new InputOption<>("Rumble Amount",
					new FloatSettingWatcher("rumbleAmount"),
					new FloatRangeInputController(0, 1, .1F)));

			sub.add(new InputOption<>("Deadzone",
					new FloatSettingWatcher("deadzone"),
					new FloatRangeInputController(1, 10, 1F)));
		}

		sub.add(new InputOption<>("Random events",
				new BooleanSettingWatcher("randomEvents"),
				new YesNoInputController()));

		sub.add(new InputOption<>("Developer mode",
				new BooleanSettingWatcher("debug"),
				new YesNoInputController()));

		sub.add(new GraphicsMenuButton());
		sub.add(new KeyBindingMenuButton());
		sub.addDefault();
		setContext(sub);
	}
}
