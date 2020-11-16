package vekta.menu.option.input;

import processing.event.KeyEvent;
import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.v;

public class InputOption<T> extends MenuOption {
	private final String name;
	private final InputWatcher<T> watcher;
	private final InputController<T> controller;

	public InputOption(String name, InputWatcher<T> watcher, InputController<T> controller) {
		this.name = name;
		this.watcher = watcher;
		this.controller = controller;
	}

	public InputWatcher<T> getWatcher() {
		return watcher;
	}

	public InputController<T> getController() {
		return controller;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void render(Menu menu, int index) {
		String name = getName();
		MenuHandle handle = menu.getHandle();
		float x = handle.getItemX();
		float y = handle.getY() + handle.getItemY(index);
		float xOffset = handle.getItemWidth() / 2F;
		boolean selected = menu.getIndex() == index;
		String valueText = getController().getName(getWatcher().getValue());

		v.fill(selected ? 255 : isEnabled() ? getColor() : 100);
		v.textAlign(v.LEFT);
		v.text(name, x - xOffset, y);
		v.textAlign(v.RIGHT);
		v.text(valueText, x + xOffset, y);

		v.textAlign(v.CENTER, v.CENTER);
	}

	@Override
	public String getSelectVerb() {
		return getController().getSelectVerb();
	}

	@Override
	public void onSelect(Menu menu) {
		getController().select(menu, getWatcher());
	}

	@Override
	public void keyPressed(Menu menu, KeyEvent event) {
		getController().keyPressed(menu, event, getWatcher());
	}

	@Override
	public boolean interceptKeyPressed(Menu menu, KeyBinding key) {
		T value = getWatcher().getValue();
		if(key == KeyBinding.MENU_LEFT && getController().hasLeft(value)) {
			getWatcher().setValue(getController().getLeft(value));
			return true;
		}
		if(key == KeyBinding.MENU_RIGHT && getController().hasRight(value)) {
			getWatcher().setValue(getController().getRight(value));
			return true;
		}
		return controller.interceptKeyPressed(menu, key, getWatcher());
	}
}