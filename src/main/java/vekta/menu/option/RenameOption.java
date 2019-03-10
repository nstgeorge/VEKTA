package vekta.menu.option;

import vekta.Renameable;
import vekta.context.TextInputContext;
import vekta.menu.Menu;

import static vekta.Vekta.setContext;

public class RenameOption implements MenuOption {
	private final Renameable renameable;

	public RenameOption(Renameable renameable) {
		this.renameable = renameable;
	}

	@Override
	public String getName() {
		return "Rename";
	}

	@Override
	public void select(Menu menu) {
		TextInputContext context = new TextInputContext(menu, "Rename:", renameable::setName);
		setContext(context);
	}
}
