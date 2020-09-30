package vekta.menu.option.input;

import vekta.KeyBinding;
import vekta.Settings;

public class KeySettingWatcher extends SettingWatcher<KeyBinding, Integer> {
	public KeySettingWatcher(KeyBinding key) {
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
