package vekta;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import processing.core.PShape;
import processing.sound.SoundFile;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static vekta.Vekta.v;

public final class Resources {
	private static final String PACKAGE = "vekta";
	private static final Reflections REFLECTIONS = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forPackage(PACKAGE))
			.setScanners(
					new SubTypesScanner().filterResultsBy(new FilterBuilder().includePackage(PACKAGE)),
					new ResourcesScanner()));

	private static final Map<String, String[]> STRINGS = new HashMap<>();
	private static final Map<String, SoundFile> SOUNDS = new HashMap<>();
	private static final Map<String, PShape> SHAPES = new HashMap<>();

	private static final char REF_BEFORE = '{';
	private static final char REF_AFTER = '}';

	private static SoundFile currentMusic;

	public static PShape logo; // TODO: generalize SVG file loading

	public static void init() {

		loadResources(Resources::addStrings, "txt");
		loadResources(Resources::addShape, "obj", "svg");
		loadResources(Resources::addSound, "wav", "mp3");

		for(String key : STRINGS.keySet()) {
			checkStrings(key, STRINGS.get(key));
		}

		logo = v.loadShape("VEKTA.svg");
	}

	private static void loadResources(BiConsumer<String, String> load, String... ext) {
		for(String path : REFLECTIONS.getResources(Pattern.compile(".+\\.(" + String.join("|", ext) + ")$"))) {
			load.accept(path, getResourceName(path));
		}
	}

	private static String getResourceName(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] getSubclassInstances(Class<T> type) {
		return REFLECTIONS.getSubTypesOf(type).stream()
				.filter(t -> !t.isMemberClass() && !Modifier.isAbstract(t.getModifiers()))
				.map(t -> {
					try {
						return t.newInstance();
					}
					catch(Exception e) {
						throw new RuntimeException("Failed to instantiate subclass: " + t.getName(), e);
					}
				})
				.toArray(i -> (T[])Array.newInstance(type, i));
	}

	private static void addStrings(String path, String key) {
		String[] strings = Arrays.stream(v.loadStrings(path))
				.map(String::trim)
				.filter(s -> !s.isEmpty() && !s.startsWith("#"))
				.toArray(String[]::new);
		if(strings.length == 0) {
			throw new RuntimeException("No strings defined in file: " + path);
		}
		if(STRINGS.containsKey(key)) {
			throw new RuntimeException("Conflicting string arrays for key: `" + key + "`");
		}
		STRINGS.put(key, strings);
	}

	public static boolean hasStrings(String key) {
		return STRINGS.containsKey(key);
	}

	public static String[] getStrings(String key) {
		String[] array = STRINGS.get(key);
		if(array == null) {
			throw new RuntimeException("No string array exists with key: " + key);
		}
		return array;
	}

	public static Map<String, Set<String>> getStringMap(String key, boolean reverse) {
		Map<String, Set<String>> map = new HashMap<>();
		for(String s : getStrings(key)) {
			String[] split = s.split(":", 2);
			String id = split[split.length > 1 && reverse ? 1 : 0].trim();
			Set<String> set = map.computeIfAbsent(id, k -> new HashSet<>());
			if(split.length > 1) {
				set.add(split[reverse ? 0 : 1].trim());
			}
		}
		return map;
	}

	public static String generateString(String type) {
		String string = v.random(getStrings(type));
		int openIndex, closeIndex;
		while((openIndex = string.indexOf(REF_BEFORE)) != -1 && (closeIndex = string.indexOf(REF_AFTER)) > openIndex) {
			string = string.substring(0, openIndex)
					+ generateString(string.substring(openIndex + 1, closeIndex))
					+ string.substring(closeIndex + 1);
		}
		return string;
	}

	public static void checkStrings(String key, String[] strings) {
		for(String string : strings) {
			int openIndex, closeIndex;
			while((openIndex = string.indexOf(REF_BEFORE)) != -1 && (closeIndex = string.indexOf(REF_AFTER)) > openIndex) {
				String ref = string.substring(openIndex + 1, closeIndex);
				if(!STRINGS.containsKey(ref)) {
					throw new RuntimeException("Missing string type: `" + ref + "` (found in `" + key + "`)");
				}
				string = string.substring(0, openIndex) + string.substring(closeIndex + 1);
			}
		}
	}

	private static void addSound(String path, String key) {
		SoundFile sound = new SoundFile(v, path);
		if(STRINGS.containsKey(key)) {
			throw new RuntimeException("Conflicting sounds for key: `" + key + "`");
		}
		SOUNDS.put(key, sound);
	}

	public static boolean hasSound(String key) {
		return SOUNDS.containsKey(key);
	}

	public static SoundFile getSound(String key) {
		SoundFile sound = SOUNDS.get(key);
		if(sound == null) {
			throw new RuntimeException("No sound exists with key: " + key);
		}
		return sound;
	}

	private static void addShape(String path, String key) {
		PShape shape = v.loadShape(path);
		if(SHAPES.containsKey(key)) {
			throw new RuntimeException("Conflicting shapes for key: `" + key + "`");
		}
		SHAPES.put(key, shape);
	}

	public static PShape getShape(String key) {
		PShape shape = SHAPES.get(key);
		if(shape == null) {
			throw new RuntimeException("No string array exists with key: " + key);
		}
		return shape;
	}

	// TODO: DRY up these sound methods a bit

	public static void playSound(String key) {
		if(Settings.getInt("sound") > 0) {
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
		if(Settings.getInt("sound") > 0) {
			SoundFile sound = getSound(key);
			if(!sound.isPlaying()) {
				sound.loop();
			}
		}
	}

	public static SoundFile getMusic() {
		if(currentMusic != null && !currentMusic.isPlaying()) {
			currentMusic = null;
		}
		return currentMusic;
	}

	public static void setMusic(String key, boolean loop) {
		setMusic(getSound(key), loop);
	}

	public static void setMusic(SoundFile sound, boolean loop) {
		float volume = Settings.getInt("music");
		if(volume > 0) {
			if(sound != currentMusic) {
				// Stop previous music
				if(currentMusic != null) {
					currentMusic.stop();
				}
				// Start new music
				sound.amp(volume);
				if(loop) {
					sound.loop();
				}
				// Keep track for next time
				currentMusic = sound;
			}
		}
	}

	public static void stopMusic() {
		if(currentMusic != null) {
			currentMusic.stop();
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
			currentMusic.amp(Settings.getInt("music"));
		}
	}
}
