package vekta;

import processing.core.PShape;
import processing.sound.SoundFile;

import java.util.HashMap;
import java.util.Map;

import static processing.core.PApplet.println;
import static vekta.Vekta.concat;
import static vekta.Vekta.v;

public class Resources {
	private static final Map<String, String[]> STRING_ARRAYS = new HashMap<>();
	private static final Map<String, SoundFile> SOUNDS = new HashMap<>();

	private static SoundFile currentMusic;

	public static PShape logo; // TODO: generalize SVG file loading

	public static void init() {
		String[] strings = v.loadStrings("resources.txt");
		for(String file : strings) {
			if(!file.isEmpty()) {
				int dotIndex = file.lastIndexOf(".");
				String name = file.substring(file.indexOf("/") + 1, dotIndex);
				String ext = file.substring(dotIndex + 1);
			}
		}
		//TODO remove

		// Music
		addSound("theme");
		addSound("atmosphere");

		// Sound
		addSound("laser");
		addSound("death");
		addSound("engine");
		addSound("change");
		addSound("select");
		addSound("chirp");
		addSound("hyperdriveHit");
		addSound("hyperdriveLoop");
		addSound("hyperdriveEnd");
		addSound("tractorBeam");
		addSound("land");
		addSound("explosion");

		// Name strings
		addStrings("planet_prefixes");
		addStrings("planet_suffixes");
		addStrings("planet_features");

		addStrings("item_adj_common");
		addStrings("item_adj_rare");
		addStrings("item_nouns");
		addStrings("item_modifiers");
		addStrings("ore_common");
		addStrings("ore_rare");

		// Overview strings
		addStrings("overview_urban");
		addStrings("overview_rural");

		logo = v.loadShape("VEKTA.svg");
	}

	private static void addStrings(String key) {
		String[] strings = v.loadStrings("text/" + key + ".txt");
		STRING_ARRAYS.put(key, strings);
	}

	private static void addSound(String key) {
		SoundFile sound = new SoundFile(v, "sound/" + key + ".wav");
		SOUNDS.put(key, sound);
	}

	public static String[] getStrings(String key) {
		String[] array = STRING_ARRAYS.get(key);
		if(array == null) {
			throw new RuntimeException("No string array with key: " + key);
		}
		return array;
	}

	public static String[] getStrings(String key, String... extra) {
		return concat(getStrings(key), extra);
	}

	public static SoundFile getSound(String key) {
		return SOUNDS.get(key);
	}

	public static SoundFile getMusic() {
		return currentMusic;
	}

	// TODO: DRY up these sound methods a bit

	public static void playSound(String key) {
		if(Settings.get("sound") > 0) {
			SoundFile sound = getSound(key);
			if(sound != null) {
				sound.stop(); // TODO: confirm whether this is needed
				sound.play();
			}
			else {
				println("Sound is not registered: " + key);
			}
		}
	}

	public static void stopSound(String key) {
		SoundFile sound = getSound(key);
		if(sound != null) {
			sound.stop();
		}
	}

	public static void stopAllSounds() {
		for(SoundFile sound : SOUNDS.values()) {
			sound.stop();
		}
	}

	public static void loopSound(String key) {
		if(Settings.get("sound") > 0) {
			SoundFile sound = getSound(key);
			if(sound != null && !sound.isPlaying()) {
				sound.loop();
			}
		}
	}

	public static void setMusic(String key) {
		float volume = Settings.get("music");
		if(volume > 0) {
			SoundFile sound = getSound(key);
			if(sound != currentMusic) {
				// Stop previous music
				if(currentMusic != null) {
					currentMusic.stop();
				}
				// Start new music
				if(sound != null) {
					sound.amp(volume);
					sound.loop();
				}
				// Keep track for next withTime
				currentMusic = sound;
			}
		}
	}

	public static void setSoundVolume(String key, float volume) {
		SoundFile sound = getSound(key);
		if(sound != null) {
			if(volume > 0) {
				sound.amp(volume);
			}
			else {
				sound.stop();
			}
		}
	}

	public static void setSoundPan(String key, float pan) {
		SoundFile sound = getSound(key);
		if(sound != null) {
			sound.pan(pan);
		}
	}

	public static void resetSoundVolumeAndPan(String key) {
		SoundFile sound = getSound(key);
		if(sound != null) {
			sound.pan(0);
			sound.amp(1);
		}
	}

	public static void updateMusicVolume() {
		if(currentMusic != null) {
			currentMusic.amp(Settings.get("music"));
		}
	}
}
