package vekta.menu.handle;

import vekta.KeyBinding;

public class InternetMenuHandle extends MenuHandle {
	public InternetMenuHandle() {
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_INTERNET;
	}
}
