package vekta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import processing.core.PShape;
import processing.sound.SoundFile;
import vekta.config.Config;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static processing.core.PApplet.println;
import static vekta.Vekta.v;

public final class Resources {
	private static final String PACKAGE = "vekta";
	private static final Reflections REFLECTIONS = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forPackage(PACKAGE))
			.setScanners(
					new SubTypesScanner().filterResultsBy(new FilterBuilder().includePackage(PACKAGE)),
					new ResourcesScanner()));

	private static final ObjectMapper CONFIG_MAPPER = new ObjectMapper();

	private static final Map<String, Config> CONFIGS = new HashMap<>();
	private static final Map<String, String[]> STRINGS = new HashMap<>();
	private static final Map<String, SoundFile> SOUNDS = new HashMap<>();
	private static final Map<String, PShape> SHAPES = new HashMap<>();

	private static final char REF_BEFORE = '{';
	private static final char REF_AFTER = '}';

	private static final int MUSIC_FADE_TIME = 100;

	private static SoundFile prevMusic;
	private static SoundFile currentMusic;
	private static int fadeProgress;

	private static float soundVolume;
	private static float musicVolume;

	public static PShape logo; // TODO: generalize SVG file loading

	public static void init() {
		adjustFromSettings();

		loadResources(Resources::addStrings, "txt");
		loadResources(Resources::addConfigs, "json");
		loadResources(Resources::addShape, "obj", "svg");
		loadResources(Resources::addSound, "wav", "mp3");

		for(String key : STRINGS.keySet()) {
			checkStrings(key, STRINGS.get(key));
		}

		logo = v.loadShape("vekta_wordmark.svg");
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
	public static <T> T[] findSubclassInstances(Class<T> type) {
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

	private static void addConfigs(String path, String key) {
		if(CONFIGS.containsKey(key)) {
			throw new RuntimeException("Multiple config files for key: `" + key + "`");
		}
		try {
			Config config = CONFIG_MAPPER.readValue(Objects.requireNonNull(Resources.class.getResourceAsStream("/" + path)), Config.class);
			CONFIGS.put(key, config);
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to load config: " + path, e);
		}
	}

	public static Config getConfig(String key) {
		Config config = CONFIGS.get(key);
		if(config == null) {
			throw new RuntimeException("No config data found for key: `" + key + "`");
		}
		return config;
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

	public static Map<String, List<String>> getStringMap(String key, boolean reverse) {
		Map<String, List<String>> map = new HashMap<>();
		for(String s : getStrings(key)) {
			String[] split = s.split(":", 2);
			String id = split[split.length > 1 && reverse ? 1 : 0].trim();
			List<String> list = map.computeIfAbsent(id, k -> new ArrayList<>());
			if(split.length > 1) {
				list.add(split[reverse ? 0 : 1].trim());
			}
		}
		return map;
	}

	public static String generateString(String key) {
		return parseString(v.random(getStrings(key)));
	}

	public static String parseString(String string) {
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
			throw new RuntimeException("No shape exists with key: " + key);
		}
		return shape;
	}

	// TODO: DRY up these sound methods a bit

	public static void playSound(String key) {
		playSound(key, 1);
	}

	public static void playSound(String key, float volume) {
		playSound(key, volume, 0);
	}

	public static void playSound(String key, float volume, float pan) {
		playSound(key, volume, pan, 1);
	}

	public static void playSound(String key, float volume, float pan, float freq) {
		volume *= soundVolume;
		if(volume > 0) {
			SoundFile sound = getSound(key);
			sound.stop();
			if(sound.channels() > 1) {//TODO refactor
				println(":: Stereo sound:", key);
			}
			sound.play(freq, pan, volume);
		}
	}

	public static void randomizeSoundProgress(String key) {
		SoundFile sound = getSound(key);
		randomizeSoundProgress(sound);
	}

	private static void randomizeSoundProgress(SoundFile sound) {
		sound.jump(v.random(sound.duration()));
	}

	public static void stopSound(String key) {
		getSound(key).stop();
	}

	public static void stopAllSounds() {
		for(SoundFile sound : SOUNDS.values()) {
			sound.stop();
		}
	}

	public static void stopAllSoundsExceptMusic() {
		for(SoundFile sound : SOUNDS.values()) {
			if(sound != currentMusic && sound != prevMusic) {
				sound.stop();
			}
		}
	}

//	public static void loopSound(String key) {
//		loopSound(key, false);
//	}

	public static void loopSound(String key/*, boolean randomize*/) {
		if(soundVolume > 0) {
			SoundFile sound = getSound(key);
			if(!sound.isPlaying()) {
				sound.loop();
//				if(randomize) {
//					randomizeSoundProgress(sound);
//				}
			}
		}
	}

	public static SoundFile getMusic() {
		return currentMusic;
	}

	public static void setMusic(String key, boolean loop) {
		setMusic(getSound(key), loop);
	}

	public static void setMusic(SoundFile sound, boolean loop) {
		if(musicVolume > 0 && sound != currentMusic) {
			// Set up crossfading
			stopMusic();
			currentMusic = sound;

			// Play sound
			sound.amp(musicVolume);
			if(loop) {
				sound.loop();
				randomizeSoundProgress(sound);
			}
			else {
				sound.play();
			}
		}
	}

	public static void stopMusic() {
		if(prevMusic != null) {
			prevMusic.stop();
			prevMusic = null;
		}
		if(currentMusic != null) {
			prevMusic = currentMusic;
			currentMusic = null;
		}
		fadeProgress = 0;
	}

	public static void adjustFromSettings() {
		soundVolume = Settings.getBoolean("muteSound") ? 0 : Settings.getFloat("sound");
		musicVolume = Settings.getBoolean("muteMusic") ? 0 : Settings.getFloat("music");

		if(currentMusic != null) {
			currentMusic.amp(musicVolume);
		}
	}

	public static void updateAudio() {
		if(currentMusic != null && currentMusic.percent() > 100) {
			currentMusic.stop();
			currentMusic = null;
		}

		if(prevMusic != null) {
			if(prevMusic.isPlaying() && fadeProgress < MUSIC_FADE_TIME) {
				fadeProgress++;
				float progress = (float)fadeProgress / MUSIC_FADE_TIME;
				prevMusic.amp(musicVolume * (1 - progress));
				if(currentMusic != null) {
					currentMusic.amp(musicVolume * progress);
				}
			}
			else {
				prevMusic.stop();
				prevMusic = null;
				if(currentMusic != null) {
					currentMusic.amp(musicVolume);
				}
				fadeProgress = 0;
			}
		}
	}
}
