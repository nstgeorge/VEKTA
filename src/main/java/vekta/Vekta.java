package vekta;

import ch.bildspur.postfx.builder.PostFX;
import com.github.strikerx3.jxinput.XInputAxes;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.enums.XInputButton;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;
import com.github.strikerx3.jxinput.listener.SimpleXInputDeviceListener;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;
import vekta.audio.AudioDriver;
import vekta.context.Context;
import vekta.context.DisplayContext;
import vekta.context.PauseMenuContext;
import vekta.context.StartSceneContext;
import vekta.item.ItemType;
import vekta.menu.Menu;
import vekta.menu.handle.MainMenuHandle;
import vekta.menu.option.ExitGameButton;
import vekta.menu.option.SettingsMenuButton;
import vekta.menu.option.WorldButton;
import vekta.shader.ScanLinePass;
import vekta.sync.Syncable;
import vekta.world.Multiplayer;
import vekta.world.RenderLevel;
import vekta.world.Singleplayer;
import vekta.world.World;

import java.io.File;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Core class for all of Vekta.
 */
public class Vekta extends PApplet {
	private static final Logger LOG = Logger.getLogger(Vekta.class.getName());

	//	static {
	//		// Prevent library startup messages
	//		LogManager.getLogManager().reset();
	//	}

	public static Vekta v; // Global access to Vekta instance

	private static final File DIRECTORY = new File("vekta_data");

	private static final Random RANDOM = new Random();

	private static PostFX postFX;

	public static Menu mainMenu;

	private static Context context;
	private static Context nextContext; // Context to become active next frame
	private static World world; // Most recent world context

	// Game-balancing variables and visual settings
	public static final float MIN_GRAVITY_MASS = 1e23F; // Minimum mass for gravity-imparting planets
	public static final float EARTH_ATMOSPHERE_LIMIT = 1e8F;

	// Render/spawning distances (we might want to use kilometers due to limited floating-point precision)
	public static final float DETAIL_LEVEL = 1e1F;
	public static final float SHIP_LEVEL = 1e2F;
	public static final float ATMOSPHERE_LEVEL = 1e5F;
	public static final float PLANET_LEVEL = 1e7F;
	public static final float STAR_LEVEL = 3e8F;
	public static final float INTERSTELLAR_LEVEL = 3e9F;

	public static final float MIN_ZOOM_LEVEL = .5F;

	public static final float CONTROLLER_DEADZONE = .15F;

	// Reference constants
	public static final float EARTH_MASS = 5.9736e24F;
	public static final float LUNAR_MASS = 7.3476e22F;
	public static final float SUN_MASS = 1.989e30F;
	public static final float EARTH_RADIUS = 6.3711e6F;
	public static final float SUN_RADIUS = 6.96340e8F;
	public static final float AU_DISTANCE = 1.496e11F;
	public static final float LUNAR_DISTANCE = 3.844e8F;
	public static final float G = 6.674e-11F;                    // Gravitational constant
	public static final float GAS_CONSTANT = 8.3143F;            // Universal gas constant
	public static final double STEFAN_BOLTZMANN = 5.670373e-8;  // Stefan-Boltzmann constant, used to calculate temperature and luminosity related values

	public static int UI_COLOR;
	public static int BUTTON_COLOR;
	public static int DANGER_COLOR;
	public static int MISSION_COLOR;

	public static final String FONTNAME = "font/undefined-medium.ttf";
	public static PFont HEADER_FONT;
	public static PFont BODY_FONT;

	public static XInputDevice DEVICE;

	@Override
	public void settings() {
		v = this;

		Settings.init();
		Resources.initStrings();

		pixelDensity(displayDensity());
		System.setProperty("jogl.disable.openglcore", "true");
		if(Settings.getBoolean("fullscreen")) {
			fullScreen(P2D);
		}
		else {
			size(Settings.getInt("resolutionWidth"), Settings.getInt("resolutionHeight"), P2D);
		}

		println("Starting VEKTA " + (Settings.getBoolean("fullscreen") ? "fullscreen" : "windowed") + " at " + Settings.getInt("resolutionWidth") + "x" + Settings.getInt("resolutionHeight") + " with pixel density " + displayDensity());

		noSmooth();
	}

	public void setup() {
		noCursor();
		background(0);
		postFX = new PostFX(this, width, height);

		Resources.init();
		AudioDriver.init();

		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_TEXTURE_MIPMAPS);
		((PGraphicsOpenGL)g).textureSampling(2);

		UI_COLOR = color(0, 255, 0);
		BUTTON_COLOR = color(8);
		DANGER_COLOR = color(220, 100, 0);
		MISSION_COLOR = ItemType.MISSION.getColor();

		try {
			if(System.getProperty("os.name").contains("Windows")) {
				// Retrieve the controller device
				DEVICE = XInputDevice.getDeviceFor(0);
				DEVICE.addListener(new SimpleXInputDeviceListener() {
					public void buttonChanged(XInputButton button, boolean pressed) {
						if(pressed) {
							context.buttonPressed(button);
						}
						else {
							context.buttonReleased(button);
						}
					}
				});
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		// Fonts
		HEADER_FONT = createFont(FONTNAME, 72);
		BODY_FONT = createFont(FONTNAME, 24);
		v.textFont(BODY_FONT);

		// Build main menu
		mainMenu = new Menu(null, new ExitGameButton("Quit"), new MainMenuHandle());
		mainMenu.add(new WorldButton("Singleplayer", Singleplayer::new));
		mainMenu.add(new WorldButton("Multiplayer", Multiplayer::new));
		mainMenu.add(new SettingsMenuButton());
		mainMenu.addDefault();

		// Start opening scene
		StartSceneContext startScene = new StartSceneContext();
		setContext(startScene);
		applyContext();

//		setContext(new DisplayContext(Resources.createUI("Main")));////////////
//		applyContext();

		frame.toFront();
		frame.requestFocus();

		frameRate(60);
	}

	@Override
	public void draw() {
		if(DEVICE != null) {
			DEVICE.poll(); //Xbox action listener
			analogTriggerResponse();
		}

		// Update sound state
		AudioDriver.getStudio().update();

		applyContext();
		if(context != null) {
			context.render();

			if(!(context instanceof PauseMenuContext)) {
				// TODO: fix the pause menu

				postFX.render()
						.bloom(0.8f, (int)Settings.getFloat("bloomIntensity"), 50)
						.noise(0.005f * Settings.getFloat("noiseAmount"), 0.7f)
						.custom(new ScanLinePass())
						.compose();
			}
		}

		Resources.updateAudio();
	}

	public void analogTriggerResponse() {
		// Poll analog controls regardless of if they're pressed at all or not.
		XInputAxes axes = DEVICE.getComponents().getAxes();

		// If you are accelerating you cannot decelerate at the same time with analog and vice versa.
		boolean left = axes.lt != 0;
		boolean right = axes.rt != 0;
		if(left && !right) {
			context.analogKeyPressed(-axes.lt);
		}
		if(right && !left) {
			context.analogKeyPressed(axes.rt);
		}

		context.controlStickMoved(axes.lx, axes.ly, LEFT);
		context.controlStickMoved(axes.rx, axes.ry, RIGHT);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if((world != null && world.globalKeyPressed(event)) || context != null) {
			context.keyPressed(event);
			for(KeyBinding key : KeyBinding.values()) {
				if(Settings.getKeyCode(key) == event.getKeyCode()) {
					context.keyPressed(key);
				}
			}
			if(key == ESC) {
				key = 0; // Suppress default behavior (exit)
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(context != null) {
			context.keyReleased(event);
			for(KeyBinding key : KeyBinding.values()) {
				if(Settings.getKeyCode(key) == event.getKeyCode()) {
					context.keyReleased(key);
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if(context != null) {
			context.keyTyped(event.getKey());
		}
	}

	@Override
	public void mousePressed() {
		if(context != null) {
			switch(v.mouseButton) {
			case LEFT:
				context.keyPressed(KeyBinding.SHIP_ATTACK);
				context.keyPressed(KeyBinding.MENU_SELECT);
				break;
			case RIGHT:
				context.keyPressed(KeyBinding.SHIP_DEFEND);
				break;
			case CENTER:
				context.keyPressed(KeyBinding.SHIP_KNOWLEDGE);
				break;
			}
		}
	}

	@Override
	public void mouseReleased() {
		if(context != null) {
			context.keyReleased(KeyBinding.SHIP_ATTACK);
			context.keyReleased(KeyBinding.MENU_SELECT);
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		if(context != null) {
			context.mouseWheel(event.getCount());
		}
	}

	public static Context getNextContext() {
		return nextContext;
	}

	public static Context getContext() {
		return context;
	}

	public static World getWorld() {
		if(world == null) {
			throw new RuntimeException("No world was loaded");
		}
		return world;
	}

	public static void setContext(Context context) {
		if(context == null) {
			throw new RuntimeException("Context cannot be set to null");
		}
		nextContext = context;
	}

	public static void applyContext() {
		if(nextContext == context) {
			nextContext = null;
		}
		else if(nextContext != null) {
			if(context != null) {
				context.unfocus();
			}
			context = nextContext;
			nextContext = null;
			if(context instanceof World) {
				world = (World)context;
			}
			context.focus();
		}
	}

	public static float getDistanceUnit(RenderLevel distance) {
		switch(distance) {
		case PARTICLE:
			return DETAIL_LEVEL;
		case SHIP:
			return SHIP_LEVEL;
		case ATMOSPHERE:
			return ATMOSPHERE_LEVEL;
		case PLANET:
			return PLANET_LEVEL;
		case STAR:
			return STAR_LEVEL;
		case INTERSTELLAR:
			return INTERSTELLAR_LEVEL;
		default:
			throw new RuntimeException("Unknown distance unit: " + distance);
		}
	}

	public static RenderLevel getRenderDistance(float unit) {
		return unit <= DETAIL_LEVEL ?
				RenderLevel.PARTICLE :
				unit <= SHIP_LEVEL ?
						RenderLevel.SHIP :
						unit <= ATMOSPHERE_LEVEL ?
								RenderLevel.ATMOSPHERE :
								unit <= PLANET_LEVEL ?
										RenderLevel.PLANET :
										unit <= STAR_LEVEL ?
												RenderLevel.STAR :
												RenderLevel.INTERSTELLAR;
	}

	//// Static world-related methods ////

	public static long randomID() {
		return RANDOM.nextLong();
	}

	/**
	 * Convenience method: register object for the current world.
	 */
	public static <T extends Syncable> T register(T object) {
		if(object == null) {
			return null;
		}
		return getWorld().register(object);
	}

	public static File createGameDirectory() {
		if(DIRECTORY.mkdirs()) {
			LOG.info("Created directory: " + DIRECTORY.getPath());
		}
		return DIRECTORY;
	}

	public static File createGameDirectory(String path) {
		File file = new File(DIRECTORY, path);
		if(file.mkdirs()) {
			LOG.info("Created directory: " + file.getPath());
		}
		return file;
	}

	//	/**
	//	 * Convenience method: apply one Syncable data structure to the other.
	//	 */
	//	@SuppressWarnings("unchecked")
	//	public static void syncAll(Iterable<? extends Syncable> sync, Iterable<? extends Syncable> data) {
	//		if(sync == null || data == null) {
	//			return;
	//		}
	//		for(Syncable s : sync) {
	//			for(Syncable d : data) {
	//				if(s.getSyncID() == d.getSyncID()) {
	//					s.onSync(d.getSyncData());
	//				}
	//			}
	//		}
	//	}

	//// Misc. UI methods ////

	public static String roundString(float value) {
		if(value >= 100) {
			return String.valueOf(round(value));
		}
		else if(value >= 10) {
			return String.format("%.1f", value);
		}
		return String.format("%.2f", value);
	}

	public static String quantityString(int quantity) {
		if(quantity >= 1e9) {
			return roundString(quantity / 1e9F) + "B";
		}
		if(quantity >= 1e6) {
			return roundString(quantity / 1e6F) + "M";
		}
		else if(quantity >= 1e3) {
			return roundString(quantity / 1e3F) + "k";
		}
		return String.valueOf(quantity);
	}

	public static String moneyString(String text, int money) {
		return text + (money == 0 ? "" : " [" + money + " G]");
	}

	public static String distanceString(float dist) {
		if(dist >= AU_DISTANCE) {
			return roundString(dist / AU_DISTANCE) + " AU";
		}
		else if(dist >= 1e9F) {
			return roundString(dist / 1e9F) + " Bm";
		}
		else if(dist >= 1e6F) {
			return roundString(dist / 1e6F) + " Mm";
		}
		else if(dist >= 1e3F) {
			return roundString(dist / 1e3F) + " km";
		}
		return roundString(dist) + " m";
	}

	public static String radiusString(float radius) {
		if(radius >= SUN_RADIUS * .1F) {
			return roundString(radius / SUN_RADIUS) + " Suns";
		}
		if(radius >= EARTH_RADIUS * .1F) {
			return roundString(radius / EARTH_RADIUS) + " Earths";
		}
		else if(radius >= 1e9F) {
			return roundString(radius / 1e9F) + " Bm";
		}
		else if(radius >= 1e6F) {
			return roundString(radius / 1e6F) + " Mm";
		}
		else if(radius >= 1e3F) {
			return roundString(radius / 1e3F) + " km";
		}
		return roundString(radius) + " m";
	}

	public static String massString(float mass) {
		if(mass >= SUN_MASS * .1F) {
			return roundString(mass / SUN_MASS) + " Suns";
		}
		else if(mass >= EARTH_MASS * .1F) {
			return roundString(mass / EARTH_MASS) + " Earths";
		}
		else if(mass >= LUNAR_MASS * .1F) {
			return roundString(mass / LUNAR_MASS) + " Lunars";
		}
		else if(mass >= 1e5F) {
			int order = 4;
			mass /= 1e4;
			while(mass >= 10) {
				mass /= 10;
				order++;
			}
			return roundString(mass) + "*10^" + order + " kg";
		}
		return roundString(mass) + " kg";
	}

	public static String densityString(float density) {
		return roundString(density) + " kg/m^3";
	}

	public static String atmosphereString(float atmosphereDensity) {
		//		if(atmosphereDensity >= .1) {
		//			return roundString(atmosphereDensity) + " Earths";
		//		}
		//		return String.format("%.3f Earths", atmosphereDensity);
		return densityString(atmosphereDensity);
	}

	public static String temperatureStringCelsius(float temperature) {
		return roundString(temperature) + " C";
	}

	//// Utility methods ////

	public <T> T random(T[] array) {
		return array[(int)random(array.length)];
	}

	public <T> T random(List<T> list) {
		return list.get((int)random(list.size()));
	}

	public <T> T random(Collection<T> collection) {
		return random(new ArrayList<>(collection));
	}

	public boolean chance(float chance) {
		return chance > 0 && (chance == 1 || v.random(1) < chance);
	}

	public float gaussian(float scale) {
		return (float)(RANDOM.nextGaussian() * scale);
	}

	public float normalizeAngle(float angle) {
		// Account for negative angles less than 2pi
		return ((angle % TWO_PI) + TWO_PI) % TWO_PI;
	}

	public float deltaAngle(float a, float b) {
		float result = normalizeAngle(b - a);
		return result > PI ? TWO_PI - result : result;
	}

	/**
	 * Compute the squared distance between two vectors
	 */
	public static float distSq(PVector a, PVector b) {
		float x = a.x - b.x;
		float y = a.y - b.y;
		return x * x + y * y;
	}

	/**
	 * Round with low precision to avoid binary/decimal conversion artifacts
	 */
	public static float roundEpsilon(float f) {
		return (float)(Math.round(f * 1E5F) * 1E-5);
	}

	/**
	 * Capitalize the first letter of the given string
	 */
	public static String capitalize(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

	//// Patches to existing Processing methods ////

	/**
	 * Redirect `ch.bildspur.postfx.pass.BasePass` shaders to classpath
	 */
	@Override
	public PShader loadShader(String fragFilename) {
		if(fragFilename.endsWith(".glsl")) {
			File file = new File(fragFilename);
			if(file.isAbsolute()) {
				fragFilename = "shader/external/" + file.getName();
				println("Redirecting shader (" + file.getPath() + ") to classpath (" + fragFilename + ")");
			}
		}
		return super.loadShader(fragFilename);
	}
}
