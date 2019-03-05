package vekta;

import processing.data.JSONObject;

import static vekta.Vekta.v;

public class Controls {
    private static final String PATH = System.getProperty("user.dir") + "/controls.json";

    private static JSONObject defaults;
    private static JSONObject controls;

    public static void init() {
        // Default controls
        defaults = new JSONObject();
        defaults.put("forward", 'w');
        defaults.put("backward", 's');
        defaults.put("left", 'a');
        defaults.put("right", 'd');
        defaults.put("fire", 'x');
        defaults.put("select", 's');
        defaults.put("missions", 'q');
        defaults.put("hyperdrive", '~');
        defaults.put("autopilotLand", "TAB");
        defaults.put("targeting", 't');

        // Settings
        try {
            controls = v.loadJSONObject(PATH);
        }
        catch(NullPointerException e) {
            System.out.println("controls.json not found. Using default controls.");
            controls = defaults;
            v.saveJSONObject(controls, PATH);
        }
    }

    public static char get(String key) {
        if(!controls.isNull(key)) {
            if(controls.getString(key).equals("TAB")) {
                return '\t';
            }
            return (char)controls.getInt(key);
        }
        else {
            return (char)defaults.getInt(key, 0);
        }
    }

    public static void set(String key, int value) {
        controls.setInt(key, value);
        save();
    }

    private static void save() {
        v.saveJSONObject(controls, PATH);
    }

}
