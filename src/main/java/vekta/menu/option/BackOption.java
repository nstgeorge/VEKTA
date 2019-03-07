package vekta.menu.option;

import vekta.context.Context;
import vekta.menu.Menu;

import static vekta.Vekta.setContext;

public class BackOption implements MenuOption {
	private final Context parent;
	private final Runnable callback;

	public BackOption(Context parent) {
		this(parent, null);
	}

	public BackOption(Context parent, Runnable callback) {
		this.parent = parent;
		this.callback = callback;
	}

	@Override
	public String getName() {
		return "Back";
	}

	@Override
	public void select(Menu menu) {
		setContext(parent);
		if(callback != null) {
			callback.run();
		}
		//		applyContext();
	}
}
