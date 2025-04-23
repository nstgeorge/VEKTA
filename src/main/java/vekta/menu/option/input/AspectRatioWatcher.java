package vekta.menu.option.input;

public class AspectRatioWatcher extends SettingWatcher<String, String> {

	private String selected;

	public AspectRatioWatcher() {
		super("aspectRatio");
		selected = "16x9";
	}

	@Override
	public String findSetting() {
		return selected;
	}

	@Override
	public void updateSetting(String value) {
		selected = value;
	}
}
