package vekta.audio;

import processing.sound.SoundFile;
import vekta.Resources;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.v;

public class SoundGroup implements Externalizable {
	private String key;
	private SoundFile[] sounds;

	public SoundGroup() {
	}

	public SoundGroup(String key) {
		setKey(key);
	}

	public String getKey() {
		return key;
	}

	private void setKey(String key) {
		this.key = key;
		List<SoundFile> sounds = new ArrayList<>();
		String sub;
		while(Resources.hasSound(sub = key + "_" + sounds.size())) {
			sounds.add(Resources.getSound(sub));
		}
		if(sounds.isEmpty()) {
			throw new RuntimeException("No sounds found for group: `" + key + "`");
		}
		this.sounds = sounds.toArray(new SoundFile[0]);
	}

	public int size() {
		return sounds.length;
	}

	public SoundFile get(int index) {
		return sounds[index];
	}

	public void play(int index) {
		SoundFile sound = sounds[(index + sounds.length) % sounds.length];
		sound.play();
	}

	public SoundFile random() {
		return v.random(sounds);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setKey((String)in.readObject());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getKey());
	}
}
