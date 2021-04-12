package vekta.action;

import vekta.action.runner.Runner;

public class DelayAction implements Action {

	private final float seconds;

	public DelayAction(float seconds) {
		this.seconds = seconds;
	}

	public float getSeconds() {
		return seconds;
	}

	@Override
	public void onUpdate(Runner runner) {
		if(runner.getRunTimeSeconds() >= getSeconds()) {
			runner.complete();
		}
	}
}
