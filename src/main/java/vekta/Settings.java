package vekta;

import processing.data.JSONObject;

import javax.swing.*;

import static vekta.Vekta.v;

public final class Settings {
	private static final String PATH = System.getProperty("user.dir") + "/settings.json";

	private static JSONObject defaults;
	private static JSONObject settings;

	public static void init() {
		// Default settings
		defaults = new JSONObject();
		defaults.put("sound", 1)
				.put("muteSound", false)
				.put("music", 1)
				.put("muteMusic", false)
				.put("zoomSpeed", 1)
				.put("randomEvents", true);
		for(KeyBinding key : KeyBinding.values()) {
			defaults.put(getKeyProp(key), serializeKeyCode(key.getDefaultKeyCode()));
		}

		// Settings
		try {
			settings = v.loadJSONObject(PATH);
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
