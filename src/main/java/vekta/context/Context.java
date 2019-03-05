package vekta.context;

import vekta.overlay.Overlay;

public interface Context extends Overlay {
	/**
	 * Called whenever the context is activated
	 */
	void focus();

	/**
	 * What to do when a key is pressed
	 */
	void keyPressed(char key);

	/**
	 * What to do when a key is released
	 */
	void keyReleased(char key);

	/**
	 * What to do when the mouse wheel is scrolled
	 */
	void mouseWheel(int amount);
}
