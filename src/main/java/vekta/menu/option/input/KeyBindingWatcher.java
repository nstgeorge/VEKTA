package vekta.menu.option.input;

import vekta.KeyBinding;
import vekta.Settings;

public class KeyBindingWatcher extends SettingWatcher<KeyBinding, Integer> {
	public KeyBindingWatcher(KeyBinding key) {
		super(key);
	}

	@Override
	public Integer findSetting() {
		return Settings.getKeyCode(getKey());
	}

	@Override
	public void updateSetting(Integer value) {
		Settings.set(getKey(), value);
	}
}
