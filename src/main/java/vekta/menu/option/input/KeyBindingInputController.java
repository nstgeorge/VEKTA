package vekta.menu.option.input;

import processing.event.KeyEvent;
import vekta.KeyBinding;
import vekta.Settings;
import vekta.menu.Menu;

public class KeyBindingInputController implements InputController<Integer> {
	private boolean modifying;

	public String getSelectVerb() {
		return "modify";
	}

	@Override
	public String getName(Integer value) {
		return modifying ? "???" : Settings.serializeKeyCode(value);
	}

	@Override
	public void select(Menu menu, InputWatcher<Integer> watcher) {
		modifying = true;
	}

	@Override
	public void keyPressed(Menu menu, KeyEvent event, InputWatcher<Integer> watcher) {
		if(modifying) {
			watcher.setValue(event.getKeyCode());
			modifying = false;
		}
	}

	@Override
	public boolean interceptKeyPressed(Menu menu, KeyBinding key, InputWatcher<Integer> watcher) {
		return modifying;
	}
}
