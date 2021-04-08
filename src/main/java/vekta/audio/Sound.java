package vekta.audio;

import org.fmod.jni.FMOD_STUDIO_PLAYBACK_STATE;
import org.fmod.jni.FMOD_STUDIO_STOP_MODE;
import org.fmod.studio.EventInstance;

import java.io.Serializable;

public class Sound implements Serializable {
	private final String key;
	private transient EventInstance instance;

	public Sound(String key) {
		this.key = key;

		// Eagerly load instance
		getInstance();
	}

	/**
	 * Access the underlying `EventInstance`.
	 * Only call this directly if you're doing something hacky or unusual; otherwise, just add a corresponding method in this file.
	 */
	public EventInstance getInstance() {
		if(instance == null) {
			instance = AudioDriver.getSound("event:" + key);
		}
		return instance;
	}

	public void start() {
		getInstance().start();
	}

	public void stop() {
		stop(FMOD_STUDIO_STOP_MODE.FMOD_STUDIO_STOP_ALLOWFADEOUT);
	}

	public void stop(FMOD_STUDIO_STOP_MODE mode) {
		getInstance().stop(mode);
	}

	public boolean isPlaying() {
		return getInstance().getPlaybackState() == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_PLAYING;
	}

	public boolean isPaused() {
		return getInstance().getPaused();
	}

	public void setPaused(boolean paused) {
		getInstance().setPaused(paused);
	}

	public float getVolume() {
		return getInstance().getVolume();
	}

	public void setVolume(float volume) {
		getInstance().setVolume(volume);
	}

	public float getPitch() {
		return getInstance().getPitch();
	}

	public void setPitch(float pitch) {
		getInstance().setPitch(pitch);
	}

	public float getValue(String key) {
		return getInstance().getParameterValue(key);
	}

	public void setValue(String key, float value) {
		getInstance().setParameterValue(key, value);
	}

	public void release() {
		// TODO: track unreleased sounds to help find memory leaks
		getInstance().release();
	}
}
