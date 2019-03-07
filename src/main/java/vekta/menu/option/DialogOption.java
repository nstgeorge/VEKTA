package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.dialog.Dialog;
import vekta.menu.handle.DialogMenuHandle;

import static vekta.Vekta.setContext;

public class DialogOption implements MenuOption {
	private final String name;
	private final Dialog dialog;

	public DialogOption(String name, Dialog dialog) {
		this.name = name;
		this.dialog = dialog;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void select(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new DialogMenuHandle(new BackOption(menu), dialog));
		sub.addDefault();
		setContext(sub);
	}
}
