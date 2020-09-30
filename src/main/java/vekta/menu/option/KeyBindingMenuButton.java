package vekta.menu.option;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.InputOption;
import vekta.menu.option.input.KeyBindingInputController;
import vekta.menu.option.input.KeySettingWatcher;

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
			sub.add(new InputOption<>(
					key.name().replace("_", " ").toLowerCase(),
					new KeySettingWatcher(key),
					new KeyBindingInputController()));
		}
		sub.addDefault();
		setContext(sub);
	}
}
