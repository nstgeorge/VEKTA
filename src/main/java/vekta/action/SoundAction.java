package vekta.action;

import vekta.action.runner.Runner;
import vekta.audio.Sound;

public class SoundAction implements Action {

	private final Sound sound;

	public SoundAction(Sound sound) {
		this.sound = sound;
	}

	public Sound getSound() {
		return sound;
	}

	@Override
	public void onStart(Runner runner) {
		sound.start();
	}

	@Override
	public void onUpdate(Runner runner) {
		if(sound.isStopped()) {
			runner.complete();
		}
	}

	@Override
	public void onEnd(Runner runner) {
		sound.release();
	}
}
