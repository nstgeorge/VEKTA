package vekta.menu.option.input;

import vekta.menu.Menu;

public class VolumeInputController extends FloatRangeInputController {
	private final InputWatcher<Boolean> muteWatcher;

	public VolumeInputController(float min, float max, float step, InputWatcher<Boolean> muteWatcher) {
		super(min, max, step);
		this.muteWatcher = muteWatcher;
	}

	public InputWatcher<Boolean> getMuteWatcher() {
		return muteWatcher;
	}

	@Override
	public String getName(Float value) {
		return muteWatcher.getValue() ? "(muted)" : super.getName(value);
	}

	@Override
	public String getSelectVerb() {
		return muteWatcher.getValue() ? "unmute" : "mute";
	}

	@Override
	public void select(Menu menu, InputWatcher<Float> watcher) {
		muteWatcher.setValue(!muteWatcher.getValue());
	}
}
