package vekta.menu.option;

import vekta.context.Context;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.setContext;

public class BackButton implements ButtonOption {
	private final Context parent;
	private final OptionCallback callback;

	public BackButton(Context parent) {
		this(parent, null);
	}

	public BackButton(Context parent, OptionCallback callback) {
		this.parent = parent;
		this.callback = callback;
	}

	@Override
	public String getName() {
		return "Back";
	}
	
	@Override
	public void onSelect(Menu menu) {
		setContext(parent);
		if(callback != null) {
			callback.callback();
		}
		//		applyContext();
	}

	public interface OptionCallback extends Serializable {
		void callback();
	}
}
