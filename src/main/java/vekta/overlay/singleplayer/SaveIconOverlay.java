package vekta.overlay.singleplayer;

import processing.core.PShape;
import vekta.Resources;
import vekta.overlay.PositionOverlay;

import static processing.core.PConstants.CENTER;
import static vekta.Vekta.v;

/**
 * Overlay for displaying the game's icon when the game saves.
 */
public class SaveIconOverlay extends PositionOverlay {

	private static final int SHOW_TIME = 8000;	// Time (ms) to show the icon (including fades)
	private static final int FADE_SPEED = 1000;	// Time (ms) to fade in and out
	private static final PShape logo = Resources.getShape("vekta_logo");

	private boolean showNextFrame = false;
	private boolean showing = false;
	private long startTime = 0;
	private float opacity = 0;

	private double flash;
	private double fadeIn;
	private double fadeOut;

	public SaveIconOverlay(int x, int y) {
		super(x, y);
		logo.disableStyle();
		//logo.scale(.5F);
	}

	/**
	 * Trigger the save icon next frame.
	 */
	public void trigger() {
		showNextFrame = true;
	}

	@Override
	public void render() {
		if(showNextFrame) {
			showing = true;
			showNextFrame = false;
			startTime = v.millis();
		} else if(showing) {
			// Calculate opacity for this frame
			// flash, fadeIn, and fadeOut are all [0-1], then multiplied by 255.
			flash = (.07 * Math.sin((v.millis() - startTime) / 250F)) + 0.8;
			fadeIn = Math.min(1, Math.max(0, (v.millis() - startTime) / (float)FADE_SPEED));
			fadeOut = Math.min(1, Math.max(0, ((startTime + SHOW_TIME - FADE_SPEED) - v.millis()) / (float)FADE_SPEED));
			opacity = 255 * (float)(Math.min(fadeIn, fadeOut) * flash);

			// Set opacity and draw logo
			v.pushStyle();
			v.stroke(0, 0);
			v.fill(0, 255, 0, opacity);
			v.shapeMode(CENTER);
			v.shape(logo, getX(), getY(), 78, 50);
			v.scale(.4F);
			v.popStyle();

			if(v.millis() >= startTime + SHOW_TIME) {
				showing = false;
			}
		}
	}
}
