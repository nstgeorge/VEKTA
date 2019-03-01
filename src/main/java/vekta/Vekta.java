package vekta;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.MouseEvent;
import vekta.context.Context;
import vekta.context.MainMenuContext;
import vekta.context.World;
import vekta.item.ItemType;

/**
 * Core class for all of Vekta.
 */
public class Vekta extends PApplet {
	private static Vekta instance;

	public static Vekta getInstance() {
		return instance;
	}

	public static final String FONTNAME = "font/undefined-medium.ttf";

	public static Context mainMenu;

	private static Context context;
	private static Context nextContext;

	// Game-balancing variables and visual settings
	public static final float G = 6.674e-11F;
	public static final float SCALE = 3e8F;
	public static final float MAX_G_DISTANCE = 2000;
	public static final float MAX_G_FORCE = 2F;
	public static final int MAX_PLANETS = 40;
	public static float DEF_ZOOM;
	public static int UI_COLOR;

	// Fonts
	public static PFont headerFont;
	public static PFont bodyFont;

	// HUD/Menu overlay
	private static PGraphics overlay;

	@Override
	public void settings() {
		fullScreen(P3D);
		pixelDensity(displayDensity());
	}

	public void setup() {
		instance = this;

		DEF_ZOOM = (height / 2.0F) / tan((PI * 30.0F / 180.0F)); // For some reason, this is the default eyeZ location for Processing
		UI_COLOR = color(0, 255, 0);

		Settings.init();
		Resources.init();

		background(color(0));
		frameRate(60);
		noCursor();

		textMode(SHAPE);

		// Overlay initialization
		overlay = createGraphics(width, height);
		// Fonts
		headerFont = createFont(FONTNAME, 72);
		bodyFont = createFont(FONTNAME, 24);

		mainMenu = new MainMenuContext();
		//		mainMenu = new Menu(new MainMenuHandle(new ExitGameOption("Quit")));
		//		mainMenu.addDefault();
		setContext(mainMenu);
	}

	@Override
	public void draw() {
		if(nextContext != null) {
			context = nextContext;
			nextContext = null;
		}
		
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

	public static void startWorld(World world) {
		Vekta.clearOverlay();
		Vekta.setContext(world);
		world.init();
	}

	// TODO: is this being used?
	
	public static void clearOverlay() {
		if(overlay.isLoaded()) {
			overlay.clear();
			overlay.beginDraw();
			overlay.background(0, 0);
			overlay.endDraw();
			overlay.setLoaded(false);
		}
	}

	public static void drawOverlay() {
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

	public static void setContext(Context context) {
		if(context == null) {
			throw new RuntimeException("Context cannot be set to null");
		}
		nextContext = context;
	}

	//// Generator methods (will move to another class) ////

	public static String generatePlanetName() {
		Vekta v = getInstance();
		return v.random(Resources.getStrings("planet_prefixes")) + v.random(Resources.getStrings("planet_suffixes", ""));
	}

	public static String generateItemName(ItemType type) {
		Vekta v = getInstance();
		String name = v.random(Resources.getStrings("item_nouns"));
		if(v.random(1) > .5) {
			name += " " + v.random(Resources.getStrings("item_modifiers"));
		}

		if(type == ItemType.LEGENDARY) {
			name += " of " + generatePlanetName();
		}
		else {
			if(v.random(1) > .5) {
				String adj = v.random(Resources.getStrings(type == ItemType.COMMON ? "item_adj_common" : "item_adj_rare"));
				name = adj + " " + name;
			}
		}
		return name;
	}

	public <T> T random(T[] array) {
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
