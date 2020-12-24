package vekta.menu.option;

import vekta.KeyCategory;
import vekta.menu.Menu;
import vekta.menu.handle.SettingsMenuHandle;

import static vekta.Vekta.setContext;

public class KeyBindingMenuButton extends ButtonOption {

	@Override
	public String getName() {
		return "Key Bindings";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new SettingsMenuHandle());
		for(KeyCategory category : KeyCategory.values()) {
			sub.add(new KeyCategoryButton(category));
		}
		sub.addDefault();
		setContext(sub);
	}
}
