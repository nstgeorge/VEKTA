package vekta;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;
import vekta.context.Context;
import vekta.context.World;
import vekta.menu.Menu;
import vekta.menu.handle.MainMenuHandle;
import vekta.menu.option.ExitGameOption;
import vekta.menu.option.SettingsMenuOption;
import vekta.menu.option.WorldOption;

import java.util.logging.LogManager;

/**
 * Core class for all of Vekta.
 */
public class Vekta extends PApplet {
	static {
		// Prevent library startup messages
		LogManager.getLogManager().reset();
	}

	public static Vekta v; // Global access to Vekta instance

	public static final String FONTNAME = "font/undefined-medium.ttf";

	public static Menu mainMenu;

	private static Context context;
	private static Context nextContext;

	// Game-balancing variables and visual settings
	public static final float G = 6.674e-11F;
	public static final float SCALE = 3e7F;
	//	public static final float MAX_G_DISTANCE = 5000;
	public static final float MAX_G_DISTANCE = Float.POSITIVE_INFINITY; // Phasing out in favor of other optimizations
	public static final float MAX_G_FORCE = 2F;
	public static final int MAX_PLANETS = 5;
	public static final float MAX_CAMERA_Y = 5000;
	public static float DEF_ZOOM;
	public static int UI_COLOR;

	// Fonts
	public static PFont headerFont;
	public static PFont bodyFont;

	@Override
	public void settings() {
		fullScreen(P3D);
		pixelDensity(displayDensity());
	}

	public void setup() {
		v = this;

		DEF_ZOOM = (height / 2.0F) / tan((PI * 30.0F / 180.0F)); // For some reason, this is the default eyeZ location for Processing
		UI_COLOR = color(0, 255, 0);

		Settings.init();
		Resources.init();

		background(0);
		frameRate(60);
		noCursor();

		textMode(SHAPE);

		// Fonts
		headerFont = createFont(FONTNAME, 72);
		bodyFont = createFont(FONTNAME, 24);

		//		mainMenu = new SettingsMenuContext();
		mainMenu = new Menu(new MainMenuHandle(new ExitGameOption("Quit")));
		mainMenu.add(new WorldOption("Singleplayer", new Singleplayer()));
		mainMenu.add(new SettingsMenuOption());
		mainMenu.addDefault();
		setContext(mainMenu);
		applyContext();
	}

	@Override
	public void draw() {
		applyContext();
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
	}

	@Override
	public void keyPressed() {
		if(context != null) {
			context.keyPressed(key);
			if(key == ESC) {
				key = 0; // Suppress default behavior (exit)
			}
		}
	}

	@Override
	public void keyReleased() {
		if(context != null) {
			context.keyReleased(key);
		}
	}

	@Override
	public void mousePressed() {
		if(context != null) {
			context.keyPressed('x');
		}
	}

	@Override
	public void mouseReleased() {
		if(context != null) {
			context.keyReleased('x');
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		if(context != null) {
			context.mouseWheel(event.getCount());
		}
	}

	public static void addObject(Object object) {
		getWorld().addObject(object);
	}

	public static void removeObject(Object object) {
		getWorld().removeObject(object);
	}

	public static Context getContext() {
		return context;
	}

	public static World getWorld() {
		if(!(context instanceof World)) {
			throw new RuntimeException("Current context is not a World (" + context.getClass().getName() + ")");
		}
		return (World)context;
	}

	public static void startWorld(World world) {
		Vekta.setContext(world);
		Vekta.applyContext();
		world.start();
	}

	public static void setContext(Context context) {
		if(context == null) {
			throw new RuntimeException("Context cannot be set to null");
		}
		nextContext = context;
	}

	public static void applyContext() {
		if(nextContext != null) {
			context = nextContext;
			nextContext = null;
			context.focus();
		}
	}

	//// Utility methods ////

	public <T> T random(T[] array) {
		return array[(int)random(array.length)];
	}

	/**
	 * Return the sign (-1, 0, or 1) of a number.
	 */
	public float sign(float f) {
		return f == 0 ? 0 : f > 0 ? 1 : -1;
	}

	/**
	 * Return the squared distance between two vectors.
	 */
	public static float distSq(PVector a, PVector b) {
		float x = a.x - b.x;
		float y = a.y - b.y;
		return x * x + y * y;
	}

	//// Main method ////

	public static void main(String[] argv) {
		PApplet.main(Vekta.class, argv);
	}
}
