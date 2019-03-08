package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.person.Dialog;

import static vekta.Vekta.applyContext;
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
		Menu sub = new Menu(menu.getPlayer(), new DialogMenuHandle(menu.getDefault(), dialog));
		if(dialog.getOptions().isEmpty()) {
			sub.addDefault();
		}
		else {
			for(MenuOption option : dialog.getOptions()) {
				sub.add(option);
			}
		}
		setContext(sub);
		applyContext();
	}
}
