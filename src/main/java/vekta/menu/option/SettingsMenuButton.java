package vekta.menu.option;

import com.google.common.graph.Graph;
import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.*;

import java.util.Arrays;

import static vekta.Vekta.setContext;
import static vekta.Vekta.device;
import static vekta.Vekta.OPERATING_SYSTEM;

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

		if(OPERATING_SYSTEM.contains("Windows")) {
			if(device.isConnected()) {
				sub.add(new InputOption<>("Rumble Amount",
						new FloatSettingWatcher("rumbleAmount"),
						new FloatRangeInputController(0, 1, .1F)));

				sub.add(new InputOption<>("Deadzone",
						new FloatSettingWatcher("deadzone"),
						new FloatRangeInputController(1, 10, 1F)));
			}
		}

		sub.add(new InputOption<>("Random events",
				new BooleanSettingWatcher("randomEvents"),
				new ChoicesInputController<>(Arrays.asList(true, false), b -> b ? "Yes" : "No")));

		sub.add(new InputOption<>("Zoom near planets",
				new BooleanSettingWatcher("zoomNearPlanets"),
				new ChoicesInputController<>(Arrays.asList(true, false), b -> b ? "Yes" : "No")));

		sub.add(new GraphicsMenuButton());
		sub.add(new KeyBindingMenuButton());
		sub.addDefault();
		setContext(sub);
	}
}
