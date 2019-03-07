package vekta;

import processing.core.PShape;
import processing.sound.SoundFile;

import java.util.*;

import static processing.core.PApplet.println;
import static vekta.Vekta.concat;
import static vekta.Vekta.v;

public class Resources {
	private static final Map<String, String[]> STRING_ARRAYS = new HashMap<>();
	private static final Map<String, SoundFile> SOUNDS = new HashMap<>();

	private static SoundFile currentMusic;

	public static PShape logo; // TODO: generalize SVG file loading
	
	private static final Set<String> preload = new HashSet<>();

	public static void init() {
		preload.addAll(Arrays.asList(v.loadStrings("preload.txt")));
		for(String path : preload) {
			if(!path.isEmpty()) {
				int dotIndex = path.lastIndexOf(".");
				String name = path.substring(path.indexOf("/") + 1, dotIndex);
				String ext = path.substring(dotIndex + 1);
				if("wav".equals(ext)) {
					addSound(path, name);
				}
				if("txt".equals(ext)) {
					addStrings(path, name);
				}
			}
		}

		logo = v.loadShape("VEKTA.svg");
	}

	private static String[] addStrings(String path, String key) {
		checkPreload(path);
		String[] strings = v.loadStrings(path);
		STRING_ARRAYS.put(key, strings);
		return strings;
	}

	private static SoundFile addSound(String path, String key) {
		checkPreload(path);
		SoundFile sound = new SoundFile(v, path);
		SOUNDS.put(key, sound);
		return sound;
	}
	
	private static void checkPreload(String path) {
		if(!preload.contains(path)) {
			// TODO: do this automatically
			println(":: Resource should be added to preload.txt: " + path);
		}
	}

	public static String[] getStrings(String key) {
		String[] array = STRING_ARRAYS.get(key);
		if(array == null) {
			// Load default string file
			array = addStrings("text/" + key + ".txt", key);
			//			throw new RuntimeException("No string array with key: " + key);
		}
		return array;
	}

	public static String[] getStrings(String key, String... extra) {
		return concat(getStrings(key), extra);
	}

	public static String generateString(String type, String... extra) {
		String string = v.random(getStrings(type, extra));
		int openIndex, closeIndex;
		while((openIndex = string.indexOf("{")) != -1 && (closeIndex = string.indexOf("}")) > openIndex) {
			string = string.substring(0, openIndex)
					+ generateString(string.substring(openIndex + 1, closeIndex))
					+ string.substring(closeIndex + 1);
		}
		return string;
	}

	public static SoundFile getSound(String key) {
		SoundFile sound = SOUNDS.get(key);
		if(sound == null) {
			sound = addSound("sounds" + key + ".wav", key);
		}
		return sound;
	}

	public static SoundFile getMusic() {
		return currentMusic;
	}

	// TODO: DRY up these sound methods a bit

	public static void playSound(String key) {
		if(Settings.get("sound") > 0) {
			SoundFile sound = getSound(key);
			sound.stop();
			sound.play();
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

	public static void stopAllSoundsNotMusic() {
		for(SoundFile sound : SOUNDS.values()) {
			if(sound != getMusic())
				sound.stop();
		}
	}

	public static void loopSound(String key) {
		if(Settings.get("sound") > 0) {
			SoundFile sound = getSound(key);
			if(!sound.isPlaying()) {
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
				sound.amp(volume);
				sound.loop();
				// Keep track for next withTime
				currentMusic = sound;
			}
		}
	}

	public static void setSoundVolume(String key, float volume) {
		SoundFile sound = getSound(key);
		if(volume > 0) {
			sound.amp(volume);
		}
		else {
			sound.stop();
		}
	}

	public static void setSoundPan(String key, float pan) {
		SoundFile sound = getSound(key);
		sound.pan(pan);
	}

	public static void resetSoundVolumeAndPan(String key) {
		SoundFile sound = getSound(key);
		sound.pan(0);
		sound.amp(1);
	}

	public static void updateMusicVolume() {
		if(currentMusic != null) {
			currentMusic.amp(Settings.get("music"));
		}
	}
}
