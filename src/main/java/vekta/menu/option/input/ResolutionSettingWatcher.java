package vekta.menu.option.input;

import vekta.Settings;

public class ResolutionSettingWatcher extends SettingWatcher<String, String> {

	public ResolutionSettingWatcher(String key) {
		super(key);
	}

	@Override
	public String findSetting() {
		return Settings.getInt("resolutionWidth") + "x" + Settings.getInt("resolutionHeight");
	}

	@Override
	public void updateSetting(String value) {
		String[] resolutionValues = value.split("x");
		Settings.set("resolutionWidth", Integer.parseInt(resolutionValues[0]));
		Settings.set("resolutionHeight", Integer.parseInt(resolutionValues[1]));
	}
}
