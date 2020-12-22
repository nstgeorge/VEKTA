package vekta.menu.option;

import vekta.KeyBinding;
import vekta.KeyCategory;
import vekta.menu.Menu;
import vekta.menu.handle.KeybindMenuHandle;
import vekta.menu.option.input.InputOption;
import vekta.menu.option.input.KeyBindingInputController;
import vekta.menu.option.input.KeySettingWatcher;

import java.util.Locale;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class KeyCategoryButton extends ButtonOption {

	private final KeyCategory category;
	private final String name;

	public KeyCategoryButton(KeyCategory category) {
		this.category = category;

		// Cache name
		String name = category.name().replace("_", " ");
		this.name = name.charAt(0) + name.substring(1).toLowerCase();
	}

	@Override
	public String getName() {
		return name;
	}

	public KeyCategory getCategory() {
		return category;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new KeybindMenuHandle(getName()));
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
