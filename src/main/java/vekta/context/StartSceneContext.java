package vekta.context;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;

import static processing.core.PConstants.CORNERS;
import static vekta.Vekta.*;

public class StartSceneContext implements Context {

	private static final float FADE_SPEED = 3000;        // Milliseconds which the end fade takes to complete
	private static final int FADE_DELAY = 2000;            // Milliseconds between end of the output and beginning of the fade

	private final Typewriter writer;
	private long fadeBegin;
	private boolean endFlag;

	public StartSceneContext() {
		String[] startSceneStrings = Resources.getStrings("start_scene");
		String startSceneString = String.join("\n", startSceneStrings);
		writer = new Typewriter(startSceneString, 20, 100);
	}

	@Override
	public void focus() {
		Resources.setMusic("intro_and_menu", false);
	}

	@Override
	public void render() {
		v.background(0);

		writer.render();

		if(writer.isComplete() && !endFlag) {
			fadeBegin = v.millis();
			endFlag = true;
		}

		if(endFlag) {
			float fadeRectAlpha = 255 * (float)(v.millis() - (fadeBegin + FADE_DELAY)) / FADE_SPEED;
			v.pushStyle();
			v.rectMode(CORNERS);
			v.fill(0, 0, 0, Math.min(255, fadeRectAlpha));
			v.rect(0, 0, v.width, v.height);
			v.popStyle();

			if(fadeRectAlpha >= 255) {
				setContext(mainMenu);
			}
		}
	}

	@Override
	public void keyPressed(KeyBinding key) {
		setContext(mainMenu);
	}

	private enum TypeMode {
		NORMAL(50, "typewriter_normal"),
		WHITESPACE(NORMAL.outputSpeed * 2, "typewriter_quiet"),
		ELLIPSE(3, "typewriter_special"),
		NEWLINE(2, "typewriter_special");

		private final int outputSpeed;        // Characters per second to print in this output mode
		private final String sound;            // Key of the string to play for this sound

		TypeMode(int outputSpeed, String sound) {
			this.outputSpeed = outputSpeed;
			this.sound = sound;
		}
	}

	// We can generalize this once we find a good secondary use case
	private static class Typewriter {

		private static final float VOLUME = 0.2f;

		private final String output;    // String to be revealed
		private TypeMode mode;                // Mode that the Typewriter is currently in (determines speed of output)
		private int index;                // Index of the current character
		private long lastCharTime;        // Last time a character was displayed
		private boolean paused;            // Flag to temporarily pause output

		int x, y;

		public Typewriter(String output, int x, int y) {
			this.output = output;
			this.x = x;
			this.y = y;
			lastCharTime = 0;

			this.mode = TypeMode.NORMAL;
		}

		public void render() {
			applyStyle();
			if(isComplete()) {
				v.color(255);
				v.text(output, x, y);
			}
			else {
				v.color(255);
				v.text(output.substring(0, index), x, y);

				if(v.millis() >= lastCharTime + (1000 / mode.outputSpeed) && !paused) {
					lastCharTime = v.millis();

					// Using music volume, TODO strongly typed settings
					if(!Settings.getBoolean("muteMusic")) {
						Resources.playSound(mode.sound, Settings.getFloat("music") * VOLUME, 0, 1 + v.gaussian(.01f));
					}
					index++;

					// Handle mode checking
					if(output.charAt(index - 1) == '\n') {
						mode = TypeMode.NEWLINE;
					}
					else if(output.charAt(index - 1) == ' ' || output.charAt(index - 1) == '\t') {
						mode = TypeMode.WHITESPACE;
					}
					else if(output.charAt(index - 1) == '.') {
						mode = TypeMode.ELLIPSE;
					}
					else {
						mode = TypeMode.NORMAL;
					}
				}
			}
		}

		public void pause() {
			paused = true;
		}

		public void unpause() {
			paused = false;
		}

		public boolean isComplete() {
			return index >= output.length() - 1;
		}

		public void applyStyle() {
			v.color(UI_COLOR);
		}
	}
}
