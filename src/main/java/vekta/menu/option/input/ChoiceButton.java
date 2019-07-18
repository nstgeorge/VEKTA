package vekta.menu.option.input;

import vekta.menu.Menu;
import vekta.menu.option.ButtonOption;

public class ChoiceButton<T> implements ButtonOption {
	private final InputWatcher<T> watcher;
	private final InputController<T> controller;
	private final T value;

	public ChoiceButton(InputWatcher<T> watcher, InputController<T> controller, T value) {
		this.watcher = watcher;
		this.controller = controller;
		this.value = value;
	}

	@Override
	public String getName() {
		return controller.getName(value);
	}

	@Override
	public void onSelect(Menu menu) {
		watcher.setValue(value);
		menu.close();
	}
}
