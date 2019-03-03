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

	public static int get(String key) {
		if(!settings.isNull(key)) {
			return settings.getInt(key);
		}
		else {
			return defaults.getInt(key, 0);
		}
	}

	public static void set(String key, int value) {
		settings.setInt(key, value);
		save();
	}

	private static void save() {
		v.saveJSONObject(settings, PATH);
	}
}
