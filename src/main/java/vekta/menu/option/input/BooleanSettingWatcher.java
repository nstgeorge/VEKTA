package vekta.menu.option.input;

import vekta.Settings;

public class BooleanSettingWatcher extends SettingWatcher<String, Boolean> {
	public BooleanSettingWatcher(String key) {
		super(key);
	}

	@Override
	public Boolean findSetting() {
		return Settings.getBoolean(getKey());
	}

	@Override
	public void updateSetting(Boolean value) {
		Settings.set(getKey(), value);
	}
}
