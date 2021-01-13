package vekta.menu.option;

import vekta.KeyBinding;
import vekta.KeyCategory;
import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;
import vekta.menu.option.input.InputOption;
import vekta.menu.option.input.KeyBindingInputController;
import vekta.menu.option.input.KeySettingWatcher;

import static vekta.Vekta.setContext;

public class KeyCategoryButton extends ButtonOption {

	private final KeyCategory category;

	public KeyCategoryButton(KeyCategory category) {
		this.category = category;
	}

	public KeyCategory getCategory() {
		return category;
	}

	@Override
	public String getName() {
		return getCategory().getTitle();
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new SettingsMenuHandle(getCategory()));
		for(KeyBinding key : KeyBinding.values()) {
			if(key.getCategory() == getCategory()) {
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
