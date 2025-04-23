package vekta;

import static vekta.Vekta.v;

import javax.swing.KeyStroke;

import processing.data.JSONObject;

import java.nio.file.Paths;

public final class Settings {
	private static final String PATH = Paths.get(System.getProperty("user.dir"), "settings.json").toString();

	private static JSONObject defaults;
	private static JSONObject settings;

	public static void init() {
		// Default settings
		defaults = new JSONObject();
		defaults.put("sound", .5)
				.put("muteSound", false)
				.put("music", .5)
				.put("muteMusic", false)
				.put("rumbleAmount", .7)
				.put("deadzone", 4)
				.put("zoomSpeed", 1)
				.put("randomEvents", true)
				.put("zoomNearPlanets", true)
				.put("drawTrails", true)
				.put("postprocessing", true)
				.put("bloomIntensity", 6)
				.put("noiseAmount", 10)
				.put("scanLineIntensity", 5)
				.put("fullscreen", true)
				.put("resolutionWidth", v.displayWidth)
				.put("resolutionHeight", v.displayHeight)
				.put("debug", false);
		for(KeyBinding key : KeyBinding.values()) {
			defaults.put(getKeyProp(key), serializeKeyCode(key.getDefaultKeyCode()));
		}

		// Settings
		try {
			settings = v.loadJSONObject(PATH);
			if(settings == null) {
				settings = new JSONObject();
			}
		}
		catch(NullPointerException e) {
			System.out.println("settings.json not found. Using default settings.");
			settings = defaults;
			v.saveJSONObject(settings, PATH);
		}
	}

	private static String getKeyProp(KeyBinding key) {
		return "key." + key.name().toLowerCase();
	}

	public static String serializeKeyCode(int keyCode) {
		return KeyStroke.getKeyStroke(keyCode, 0).toString().replace("pressed ", "");
	}

	public static int deserializeKeyCode(String keyText) {
		return KeyStroke.getKeyStroke("pressed " + keyText).getKeyCode();
	}

	public static int getInt(String key) {
		if(!settings.isNull(key)) {
			return settings.getInt(key);
		}
		else {
			return defaults.getInt(key);
		}
	}

	public static float getFloat(String key) {
		if(!settings.isNull(key)) {
			return settings.getFloat(key);
		}
		else {
			return defaults.getFloat(key);
		}
	}

	public static boolean getBoolean(String key) {
		if(!settings.isNull(key)) {
			return settings.getBoolean(key);
		}
		else {
			return defaults.getBoolean(key);
		}
	}

	public static boolean getBoolean(String key, boolean def) {
		if(!settings.isNull(key)) {
			return settings.getBoolean(key);
		}
		else {
			return defaults.getBoolean(key, def);
		}
	}

	public static String getString(String key) {
		if(!settings.isNull(key)) {
			return settings.getString(key);
		}
		else {
			return defaults.getString(key);
		}
	}

	public static String getString(String key, String def) {
		if(!settings.isNull(key)) {
			return settings.getString(key);
		}
		else {
			return defaults.getString(key, def);
		}
	}

	public static int getKeyCode(KeyBinding key) {
		String prop = getKeyProp(key);
		String result;
		if(!settings.isNull(prop)) {
			result = settings.getString(prop);
		}
		else {
			result = defaults.getString(prop, "");
		}
		return deserializeKeyCode(result);
	}

	public static String getKeyText(KeyBinding key) {
		return serializeKeyCode(getKeyCode(key));
	}

	// TODO: call this to set keys in settings UI
	public static void set(KeyBinding key, int value) {
		set(getKeyProp(key), serializeKeyCode(value));
	}

	public static void set(String key, int value) {
		settings.setInt(key, value);
		save();
	}

	public static void set(String key, float value) {
		settings.setFloat(key, value);
		save();
	}

	public static void set(String key, boolean value) {
		settings.setBoolean(key, value);
		save();
	}

	public static void set(String key, String value) {
		settings.setString(key, value);
		save();
	}

	private static void save() {
		v.saveJSONObject(settings, PATH);
	}
}
