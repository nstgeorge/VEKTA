package vekta;

import processing.data.JSONObject;

import static vekta.Vekta.v;

public class Settings {
	private static final String PATH = System.getProperty("user.dir") + "/settings.json";

	private static JSONObject defaults;
	private static JSONObject settings;

	public static void init() {
		// Default settings
		defaults = new JSONObject();
		defaults.put("sound", 1);
		defaults.put("music", 1);
		for(ControlKey key : ControlKey.values()) {
			defaults.put(getKeyProp(key), serializeKey(key.getDefaultKey()));
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

	private static String getKeyProp(ControlKey key) {
		return "key." + key.name().toLowerCase();
	}

	// TODO: find a way to automatically do this for all keys
	private static String serializeKey(char key) {
		if(key == ' ') {
			return "SPACE";
		}
		if(key == '\t') {
			return "TAB";
		}
		return String.valueOf(key);
	}

	private static char deserializeKey(String key) {
		if("SPACE".equals(key)) {
			return ' ';
		}
		if("TAB".equals(key)) {
			return '\t';
		}
		else if(key.length() != 1) {
			return 0; // Disable invalid keys
		}
		return key.charAt(0);
	}

	public static int get(String key) {
		if(!settings.isNull(key)) {
			return settings.getInt(key);
		}
		else {
			return defaults.getInt(key, 0);
		}
	}

	public static char getCharacter(ControlKey key) {
		String prop = getKeyProp(key);
		String result;
		if(!settings.isNull(prop)) {
			result = settings.getString(prop);
		}
		else {
			result = defaults.getString(prop, "");
		}
		return deserializeKey(result);
	}

	public static String getControlString(ControlKey key) {
		return serializeKey(getCharacter(key));
	}

	public static void set(String key, int value) {
		settings.setInt(key, value);
		save();
	}

	// TODO: call this to update keys in settings UI
	public static void set(ControlKey key, char value) {
		settings.setString(getKeyProp(key), serializeKey(value));
		save();
	}

	private static void save() {
		v.saveJSONObject(settings, PATH);
	}
}
