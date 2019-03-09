package vekta.menu.option;

import vekta.menu.Menu;
import vekta.person.Dialog;

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

//	@Override
//	public int getColor() {
//		return dialog.getPerson().getColor();
//	}

	@Override
	public void select(Menu menu) {
		dialog.openMenu(menu);
	}
}
