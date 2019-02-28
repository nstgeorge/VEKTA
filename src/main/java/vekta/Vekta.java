package vekta;

import processing.core.*;
import processing.data.JSONObject;
import processing.sound.LowPass;
import processing.sound.SoundFile;

import java.awt.event.MouseEvent;

/**
 * Core class for all of Vekta.
 */
public class Vekta extends PApplet {
	private static Vekta instance;

	public static Vekta getInstance() {
		return instance;
	}
	
	private static final String SETTINGS_PATH = System.getProperty("user.dir") + "/settings.json";

	static final String FONTNAME = "font/undefined-medium.ttf";
	static final int MAX_DISTANCE = 10000; // Maximum distance for updating objects (currently unimplemented)

	static Context mainMenu;

	private static Context context;

	// Settings
	private static JSONObject defaultSettings;
	private static JSONObject settings;

	// Game-balancing variables and visual settings

	final static float G = 6.674e-11F;
	final static float SCALE = 3e8F;
	final static int UI_COLOR = new Color(0, 255, 0).getIntValue();
	final static float VECTOR_SCALE = 5;
	final static int MAX_PLANETS = 500;
	final static int TRAIL_LENGTH = 100;
	final float DEF_ZOOM = (height / 2.0F) / tan((PI * 30.0F / 180.0F)); // For some reason, this is the default eyeZ location for Processing

	// Fonts
	static PFont headerFont;
	static PFont bodyFont;

	// HUD/Menu overlay
	static PGraphics overlay;

	// A sick logo
	static PShape logo;

	// TODO: move all these references to a designated `Resources` class

	// Sounds
	static SoundFile theme;
	static SoundFile atmosphere;
	static SoundFile laser;
	static SoundFile death;
	static SoundFile engine;
	static SoundFile change;
	static SoundFile select;
	static SoundFile chirp;

	// Name components
	private static String[] planetNamePrefixes;
	private static String[] planetNameSuffixes;
	private static String[] itemNameAdjectives;
	private static String[] itemNameNouns;
	private static String[] itemNameModifiers;

	// Low pass filter
	static LowPass lowPass;

	public void settings() {
		fullScreen(P3D);
		pixelDensity(displayDensity());
	}

	public void setup() {
		instance = this;

		background(color(0));
		frameRate(60);
		noCursor();

		createSettings();

		textMode(SHAPE);

		// Overlay initialization
		overlay = createGraphics(width, height);
		// Fonts
		headerFont = createFont(FONTNAME, 72);
		bodyFont = createFont(FONTNAME, 24);
		// Images
		logo = loadShape("VEKTA.svg");

		// All sounds and music. These must be instantiated in the main file
		// Music
		theme = new SoundFile(this, "main.wav");
		atmosphere = new SoundFile(this, "atmosphere.wav");

		// Sound
		laser = new SoundFile(this, "laser.wav");
		death = new SoundFile(this, "death.wav");
		engine = new SoundFile(this, "engine.wav");
		change = new SoundFile(this, "change.wav");
		select = new SoundFile(this, "select.wav");
		chirp = new SoundFile(this, "chirp.wav");

		planetNamePrefixes = loadStrings("text/planet_prefixes.txt");
		planetNameSuffixes = concat(loadStrings("text/planet_suffixes.txt"), new String[] {""});
		itemNameAdjectives = loadStrings("text/item_adjectives.txt");
		itemNameNouns = loadStrings("text/item_nouns.txt");
		itemNameModifiers = loadStrings("text/item_modifiers.txt");

		lowPass = new LowPass(this);

		mainMenu = new MainMenu();
		setContext(mainMenu);
	}

	public void draw() {
		if(context != null) {
			context.render();
		}

		hint(DISABLE_DEPTH_TEST);
		camera();
		noLights();

		// FPS OVERLAY
		fill(255);
		textAlign(LEFT);
		textSize(16);
		text("FPS = " + frameRate, 50, height - 20);
		//loop();
	}

	public void keyPressed() {
		if(context != null) {
			context.keyPressed(key);
			if(key == ESC) {
				key = 0; // Suppress default behavior (exit)
			}
		}
	}

	public void keyReleased() {
		if(context != null) {
			context.keyReleased(key);
		}
	}

	public void mousePressed() {
		if(context != null) {
			context.keyPressed('x');
		}
	}

	public void mouseReleased() {
		if(context != null) {
			context.keyReleased('x');
		}
	}

	public void mouseWheel(MouseEvent event) {
		if(context != null) {
			context.mouseWheel(event.getClickCount());
		}
	}

	public static void startWorld(World world) {
		Vekta.clearOverlay();
		Vekta.setContext(world);
		world.init();
	}

	static void clearOverlay() {
		if(overlay.isLoaded()) {
			overlay.clear();
			overlay.beginDraw();
			overlay.background(0, 0);
			overlay.endDraw();
			overlay.setLoaded(false);
		}
	}

	static void drawOverlay() {
		// Overlay the overlay
		// NOTE: THIS IS VERY SLOW. Use only for menus, not gameplay!
		if(overlay.isLoaded()) {
			Vekta v = getInstance();
			overlay.loadPixels();
			v.loadPixels();
			for(int i = 0; i < v.pixels.length; i++)
				if(overlay.pixels[i] != 0)
					v.pixels[i] = overlay.pixels[i];
			v.updatePixels();
			overlay.updatePixels();
			//image(overlay, 0, 0);
			//redraw();
		}
	}

	void createSettings() {
		// Default settings
		defaultSettings = new JSONObject();
		defaultSettings.put("sound", 1);
		defaultSettings.put("music", 1);
		// Settings
		try {
			settings = loadJSONObject(SETTINGS_PATH);
		}
		catch(NullPointerException e) {
			System.out.println("settings.json not found. Using default settings.");
			settings = defaultSettings;
			saveJSONObject(settings, SETTINGS_PATH);
		}
	}

	static int getSetting(String key) {
		if(!settings.isNull(key)) {
			return settings.getInt(key);
		}
		else {
			if(!defaultSettings.isNull(key)) {
				return defaultSettings.getInt(key);
			}
			else {
				return 0;
			}
		}
	}

	static void setSetting(String key, int value) {
		settings.setInt(key, value);
	}

	static void saveSettings() {
		getInstance().saveJSONObject(settings, SETTINGS_PATH);
	}

	public static boolean addObject(Object object) {
		return getWorld().addObject(object);
	}

	public static boolean removeObject(Object object) {
		return getWorld().removeObject(object);
	}

	public static Context getContext() {
		return context;
	}

	public static World getWorld() {
		if(!(context instanceof World)) {
			throw new RuntimeException("Current context is not a World");
		}
		return (World)context;
	}

	public static void setContext(Context context) {
		if(context == null) {
			throw new RuntimeException("Context cannot be set to null");
		}
		Vekta.context = context;
	}

	//// Generator methods (will move to another class) ////

	public static String generatePlanetName() {
		Vekta v = getInstance();
		return v.random(planetNamePrefixes) + v.random(planetNameSuffixes);
	}

	public static String generateItemName() {
		Vekta v = getInstance();
		String name = v.random(itemNameNouns);
		if(v.random(1) > .5) {
			name = v.random(itemNameAdjectives) + " " + name;
		}
		if(v.random(1) > .5) {
			name = name + " " + v.random(itemNameModifiers);
		}
		return name;
	}

	<T> T random(T[] array) {
		return array[(int)random(array.length)];
	}

	public static float getDistSq(PVector a, PVector b) {
		float x = a.x - b.x;
		float y = a.y - b.y;
		return x * x + y * y;
	}

	//// Main method ////

	public static void main(String[] argv) {
		PApplet.main(Vekta.class, argv);
	}
};
