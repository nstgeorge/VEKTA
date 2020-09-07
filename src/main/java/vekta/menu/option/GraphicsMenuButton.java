package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.*;

import java.util.Arrays;

import static vekta.Vekta.setContext;

public class GraphicsMenuButton implements ButtonOption {
    @Override
    public String getName() {
        return "Graphics";
    }

    @Override
    public void onSelect(Menu menu) {
        Menu sub = new Menu(menu, new SettingsMenuHandle());

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
