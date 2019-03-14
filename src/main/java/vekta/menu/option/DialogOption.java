package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.person.Dialog;

public class DialogOption implements MenuOption {
	private final String name;
	private final Dialog dialog;
	private final MenuOption defaultOption;

	public DialogOption(String name, Dialog dialog) {
		this(name, dialog, null);
	}

	public DialogOption(String name, Dialog dialog, MenuOption defaultOption) {
		this.name = name;
		this.dialog = dialog;
		this.defaultOption = defaultOption;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void select(Menu menu) {
		menu.remove(this);
		
		boolean useMenuDefault = menu.getHandle() instanceof DialogMenuHandle;
		dialog.openMenu(menu.getPlayer(),
				defaultOption != null ? defaultOption :
						useMenuDefault ? menu.getDefault() : new BackOption(menu));

	}
}
