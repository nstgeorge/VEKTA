package vekta.menu.option;

import vekta.Resources;
import vekta.Settings;
import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.*;

import java.util.Arrays;
import java.util.List;

import static vekta.Vekta.setContext;

public class GraphicsMenuButton extends ButtonOption {

	private AspectRatioWatcher aspectRatio = new AspectRatioWatcher();

    @Override
    public String getName() {
        return "Graphics";
    }

    @Override
    public void onSelect(Menu menu) {
        Menu sub = new Menu(menu, new SettingsMenuHandle());

		sub.add(new InputOption<>("Fullscreen",
				new BooleanSettingWatcher("fullscreen"),
				new ChoicesInputController<>(Arrays.asList(true, false), b -> b ? "Yes" : "No")));

		if(!Settings.getBoolean("fullscreen")) {
			sub.add(new InputOption<>("Aspect Ratio",
					aspectRatio,
					new ChoicesInputController<>(Arrays.asList("16x9", "21x9", "3x4"))));

			sub.add(new InputOption<>("Resolution",
					new ResolutionSettingWatcher("resolution"),
					new resolutionChoicesController<>(Arrays.asList(Resources.getStrings("resolutions_16:9")), Arrays.asList(Resources.getStrings("resolutions_21:9")), Arrays.asList(Resources.getStrings("resolutions_3:4")), aspectRatio)));
		}

        sub.add(new InputOption<>("Noise Amount",
                new FloatSettingWatcher("noiseAmount"),
                new FloatRangeInputController(0, 10, 1)));

        sub.add(new InputOption<>("Bloom Intensity",
                new FloatSettingWatcher("bloomIntensity"),
                new FloatRangeInputController(0, 10, 1)));

        sub.add(new InputOption<>("Scan Line Intensity",
                new FloatSettingWatcher("scanLineIntensity"),
                new FloatRangeInputController(0, 10, 1)));

        sub.add(new InputOption<>("Draw trails",
                new BooleanSettingWatcher("drawTrails"),
                new ChoicesInputController<>(Arrays.asList(true, false), b -> b ? "Yes" : "No")));

        sub.addDefault();
        setContext(sub);

    }
}
