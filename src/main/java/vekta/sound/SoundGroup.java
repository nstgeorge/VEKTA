package vekta.sound;

import processing.sound.SoundFile;
import vekta.Resources;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.v;

public class SoundGroup {
	private final SoundFile[] sounds;

	public SoundGroup(String key) {
		List<SoundFile> sounds = new ArrayList<>();
		String sub;
		while(Resources.hasSound(sub = key + "_" + sounds.size())) {
			sounds.add(Resources.getSound(sub));
		}
		this.sounds = sounds.toArray(new SoundFile[0]);
	}

	public int size() {
		return sounds.length;
	}

	public void play(int index) {
		SoundFile sound = sounds[(index + sounds.length) % sounds.length];
		sound.play();
	}

	public SoundFile random() {
		return v.random(sounds);
	}
}
