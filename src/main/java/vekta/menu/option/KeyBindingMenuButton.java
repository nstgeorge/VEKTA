package vekta.menu.option;

import vekta.KeyBinding;
import vekta.Settings;
import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.ChoicesInputController;
import vekta.menu.option.input.InputController;
import vekta.menu.option.input.InputOption;
import vekta.menu.option.input.KeyBindingWatcher;

import java.util.Collections;

import static vekta.Vekta.setContext;

public class KeyBindingMenuButton implements ButtonOption {

	@Override
	public String getName() {
		return "Key Bindings";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new SettingsMenuHandle());
		for(KeyBinding key : KeyBinding.values()) {
			// TODO custom controller for key remapping
			InputController<Integer> ctrl = new ChoicesInputController<>(Collections.singletonList(Settings.getKeyCode(key)), Settings::serializeKeyCode);
			sub.add(new InputOption<>(key.name().replace("_", " ").toLowerCase(), new KeyBindingWatcher(key), ctrl));
		}
		sub.addDefault();
		setContext(sub);
	}
}
