package vekta.menu.option.input;

import vekta.Settings;

public class FloatSettingWatcher extends SettingWatcher<String, Float> {
	public FloatSettingWatcher(String key) {
		super(key);
	}

	@Override
	public Float findSetting() {
		return Settings.getFloat(getKey());
	}

	@Override
	public void updateSetting(Float value) {
		Settings.set(getKey(), value);
	}
}
