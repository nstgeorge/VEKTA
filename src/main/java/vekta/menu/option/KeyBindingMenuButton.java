package vekta.menu.option;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.InputOption;
import vekta.menu.option.input.KeyBindingInputController;
import vekta.menu.option.input.KeySettingWatcher;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class KeyBindingMenuButton extends ButtonOption {

	@Override
	public String getName() {
		return "Key Bindings";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new SettingsMenuHandle());
		for(KeyBinding.Section section : KeyBinding.Section.values()) {
			sub.add(new KeybindMenuSubsectionButton(section.name().replace("_", " ").toLowerCase()));
		}
		sub.addDefault();
		setContext(sub);
	}
}
