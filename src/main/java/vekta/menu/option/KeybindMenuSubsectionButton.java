package vekta.menu.option;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.menu.handle.KeybindMenuHandle;
import vekta.menu.option.input.InputOption;
import vekta.menu.option.input.KeyBindingInputController;
import vekta.menu.option.input.KeySettingWatcher;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class KeybindMenuSubsectionButton extends ButtonOption {

	private String section;

	public KeybindMenuSubsectionButton(String section) {
		this.section = section;
	}
	@Override
	public String getName() {
		return section;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new KeybindMenuHandle(section));
		for(KeyBinding key : KeyBinding.values()) {
			if(key.getSection().equals(section.replace(" ", "_").toUpperCase())) {
				sub.add(new InputOption<>(
						key.name().replace("_", " ").toLowerCase(),
						new KeySettingWatcher(key),
						new KeyBindingInputController()));
			}
		}
		sub.addDefault();
		setContext(sub);
	}
}
