package vekta.tune;

import processing.sound.SoundFile;
import vekta.Resources;

import java.util.ArrayList;
import java.util.List;

public class Instrument {
	private final SoundFile[] sounds;

	public Instrument(String key) {
		List<SoundFile> sounds = new ArrayList<>();
		String sub;
		while(Resources.hasSound(sub = key + "_" + sounds.size())) {
			sounds.add(Resources.getSound(sub));
		}
		this.sounds = sounds.toArray(new SoundFile[0]);
	}

	public int getNoteRange() {
		return sounds.length;
	}

	public void play(int note) {
		SoundFile sound = sounds[(note + sounds.length) % sounds.length];
		sound.play();
	}
}
