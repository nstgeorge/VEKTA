package vekta.context;

import processing.event.KeyEvent;
import vekta.KeyBinding;
import vekta.Settings;
import vekta.overlay.Overlay;

public interface Context extends Overlay {
	/**
	 * Called whenever the context is activated
	 */
	void focus();

	/**
	 * What to do when any key is pressed
	 */
	default void keyPressed(KeyEvent event) {
		for(KeyBinding ctrl : KeyBinding.values()) {
			if(Settings.getKeyCode(ctrl) == event.getKeyCode()) {
				keyPressed(ctrl);
			}
		}
	}

	/**
	 * What to do when a mapped KeyBinding is pressed
	 */
	default void keyPressed(KeyBinding key) {
	}

	/**
	 * What to do when any key is released
	 */
	default void keyReleased(KeyEvent event) {
		for(KeyBinding ctrl : KeyBinding.values()) {
			if(Settings.getKeyCode(ctrl) == event.getKeyCode()) {
				keyReleased(ctrl);
			}
		}
	}

	/**
	 * What to do when a mapped KeyBinding is released
	 */
	default void keyReleased(KeyBinding key) {
	}

	/**
	 * What to do when any key is typed
	 */
	default void keyTyped(char key) {
	}

	/**
	 * What to do when the mouse wheel is scrolled
	 */
	default void mouseWheel(int amount) {
	}
}
