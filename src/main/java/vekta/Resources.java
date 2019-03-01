package vekta;

import processing.core.PShape;
import processing.sound.SoundFile;

import java.util.HashMap;
import java.util.Map;

import static vekta.Vekta.*;

public class Resources {
	private static final Map<String, String[]> STRING_ARRAYS = new HashMap<>();
	private static final Map<String, SoundFile> SOUNDS = new HashMap<>();

	private static SoundFile currentMusic;

	public static PShape logo; // TODO: generalize SVG loading

	public static void init() {
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

		// Name strings
		addStrings("planet_prefixes");
		addStrings("planet_suffixes");
		
		addStrings("item_adj_common");
		addStrings("item_adj_rare");
		addStrings("item_nouns");
		addStrings("item_modifiers");

		// Overview strings
		addStrings("overview_urban");
		addStrings("overview_rural");

		logo = getInstance().loadShape("VEKTA.svg");
	}

	private static void addStrings(String key) {
		String[] strings = getInstance().loadStrings("text/" + key + ".txt");
		STRING_ARRAYS.put(key, strings);
	}

	private static void addSound(String key) {
		SoundFile sound = new SoundFile(getInstance(), "sound/" + key + ".wav");
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
		}
	}

	public static void stopSound(String key) {
		SoundFile sound = getSound(key);
		if(sound != null) {
			sound.stop();
		}
	}

	public static void loopSound(String key) {
		if(Settings.get("sound") > 0) {
			SoundFile sound = getSound(key);
			if(sound != null) {
				sound.stop();
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
				// Keep track for next time
				currentMusic = sound;
			}
		}
	}

	public static void setSoundVolume(String key, float volume) {
		SoundFile sound = getSound(key);
		if(sound != null) {
			sound.amp(volume);
		} else {
			System.out.println("Sound dont exist");
		}
	}

	public static void setSoundPan(String key, float pan) {
		SoundFile sound = getSound(key);
		if(sound != null) {
			sound.pan(pan);
		}
	}

	public static void updateMusicVolume() {
		if(currentMusic != null) {
			currentMusic.amp(Settings.get("music"));
		}
	}
}
