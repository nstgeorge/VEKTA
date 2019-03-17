package vekta;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import vekta.context.Context;
import vekta.context.World;
import vekta.item.ItemType;
import vekta.menu.Menu;
import vekta.menu.handle.MainMenuHandle;
import vekta.menu.option.ExitGameOption;
import vekta.menu.option.SettingsMenuOption;
import vekta.menu.option.WorldOption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
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

	private static final Random RANDOM = new Random();

	public static Menu mainMenu;

	private static Context context;
	private static World world; // Most recent world context
	private static Context nextContext;

	// Game-balancing variables and visual settings
	public static final float G = 6.674e-11F;
	public static final float SCALE = 3e7F;
	public static final float MIN_GRAVITY_MASS = 1e23F; // Minimum mass for gravity-imparting planets

	// Render/spawning distances (we might want to use kilometers due to limited floating-point precision)
	public static final float DETAIL_LEVEL = 1e1F;
	public static final float SHIP_LEVEL = 1e2F;
	public static final float PLANET_LEVEL = 1e7F;
	public static final float STAR_LEVEL = 3e8F;

	public static final float MIN_ZOOM_LEVEL = .5F;
	public static final float MAX_ZOOM_LEVEL = STAR_LEVEL; // TODO: add modules to change max zoom level

	// Reference constants
	public static final float EARTH_MASS = 5.9736e24F;
	public static final float SUN_MASS = 1.989e30F;
	public static final float AU_DISTANCE = 1.496e11F;
	public static final float LUNAR_DISTANCE = 3.844e8F;

	public static int UI_COLOR;
	public static int DANGER_COLOR;
	public static int MISSION_COLOR;

	// Fonts
	public static PFont headerFont;
	public static PFont bodyFont;

	@Override
	public void settings() {
		fullScreen(P2D);
		pixelDensity(displayDensity());
	}

	public void setup() {
		v = this;

		//		DEF_ZOOM = (height / 2.0F) / tan((PI * 30.0F / 180.0F)); // For some reason, this is the default eyeZ location for Processing
		UI_COLOR = color(0, 255, 0);
		//		DANGER_COLOR = color(255, 0, 0);
		DANGER_COLOR = v.color(220, 100, 0);
		MISSION_COLOR = ItemType.MISSION.getColor();

		Settings.init();
		Resources.init();

		background(0);
		frameRate(60);
		noCursor();

		textMode(SHAPE);

		// Fonts
		headerFont = createFont(FONTNAME, 72);
		bodyFont = createFont(FONTNAME, 24);
		v.textFont(bodyFont);

		mainMenu = new Menu(null, new ExitGameOption("Quit"), new MainMenuHandle());
		mainMenu.add(new WorldOption("Singleplayer", Singleplayer::new));
		mainMenu.add(new WorldOption("Multiplayer", Multiplayer::new));
		mainMenu.add(new SettingsMenuOption());
		mainMenu.addDefault();
		setContext(mainMenu);
		//		setContext(new Multiplayer());///
		applyContext();
	}

	@Override
	public void draw() {
		applyContext();
		if(context != null) {
			context.render();
		}

		//		hint(DISABLE_DEPTH_TEST);
		//		camera();
		//		noLights();

		// FPS OVERLAY
		fill(255);
		textAlign(LEFT);
		textSize(16);
		text("FPS = " + frameRate, 50, height - 20);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(context != null) {
			context.keyPressed(event);
			if(key == ESC) {
				key = 0; // Suppress default behavior (exit)
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(context != null) {
			context.keyReleased(event);
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
			// TODO: allow mouse events binding to user-specified keys
			context.keyPressed(KeyBinding.SHIP_FIRE);
			context.keyPressed(KeyBinding.MENU_SELECT);
		}
	}

	@Override
	public void mouseReleased() {
		if(context != null) {
			context.keyReleased(KeyBinding.SHIP_FIRE);
			context.keyReleased(KeyBinding.MENU_SELECT);
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		if(context != null) {
			context.mouseWheel(event.getCount());
		}
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
		case PLANET:
			return PLANET_LEVEL;
		case STAR:
			return STAR_LEVEL;
		default:
			throw new RuntimeException("Unknown distance unit: " + distance);
		}
	}

	public static RenderLevel getRenderDistance(float unit) {
		return unit <= DETAIL_LEVEL ?
				RenderLevel.PARTICLE :
				unit <= SHIP_LEVEL ?
						RenderLevel.SHIP :
						unit <= PLANET_LEVEL ?
								RenderLevel.PLANET :
								RenderLevel.STAR;
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

	public static String moneyString(String text, int money) {
		return text + (money == 0 ? "" : " [" + money + " G]");
	}

	//// Utility methods ////

	public <T> T random(T[] array) {
		return array[(int)random(array.length)];
	}

	public <T> T random(List<T> list) {
		return list.get((int)random(list.size()));
	}

	public <T> T random(Collection<T> collection) {
		return random(new ArrayList<>(collection)); // TODO: optimize
	}

	public boolean chance(float chance) {
		return chance > 0 && (chance >= 1 || v.random(1) < chance);
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
