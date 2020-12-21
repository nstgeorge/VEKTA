package vekta.context;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.ui.Typewriter;

import java.util.Arrays;

import static processing.core.PConstants.CORNERS;
import static vekta.Vekta.v;

public class StartSceneContext implements Context {

	private static final float FADE_SPEED = 3000;		// Milliseconds which the end fade takes to complete
	private static final int FADE_DELAY = 2000;			// Milliseconds between end of the output and beginning of the fade

	private Typewriter writer;
	private long fadeBegin;
	private boolean endFlag;

	public StartSceneContext() {
		String[] startSceneStrings = Resources.getStrings("startScene");
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
				v.setContext(v.mainMenu);
			}
		}
	}

	@Override
	public void keyPressed(KeyBinding key) {
		v.setContext(v.mainMenu);
	}
}
