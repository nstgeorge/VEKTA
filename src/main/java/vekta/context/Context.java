package vekta.context;

import com.github.strikerx3.jxinput.enums.XInputButton;
import processing.event.KeyEvent;
import vekta.KeyBinding;
import vekta.Settings;
import vekta.overlay.Overlay;

import static processing.core.PApplet.println;

public interface Context extends Overlay {
	/**
	 * Called whenever the context is activated.
	 */
	default void focus() {
	}

	/**
	 * Called whenever the context is replaced.
	 */
	default void unfocus() {
	}

	/**
	 * What to do when a button on a controller is pressed.
	 */
	default void buttonPressed(XInputButton button) {
		for(KeyBinding key : KeyBinding.values())
		{
			if(key.getButton() == button)
			{
				keyPressed(key);
			}
		}
	}

	/**
	 * What to do when a button on a controller is released.
	 */
	default void buttonReleased(XInputButton button) {
		for(KeyBinding key : KeyBinding.values())
		{
			if(key.getButton() == button)
			{
				keyReleased(key);
			}
		}
	}

	/**
	 * What to do with one of the back controller triggers is pressed.
	 */
	default void analogKeyPressed(float value){
	}

	/**
	 * What to do when the control stick is moved.
	 */
	default void controlStickMoved(float x, float y, int side) {
	}

	/**
	 * What to do when any key is pressed.
	 */
	default void keyPressed(KeyEvent event) {
	}

	/**
	 * What to do when a mapped KeyBinding is pressed.
	 */
	default void keyPressed(KeyBinding key) {
	}

	/**
	 * What to do when any key is released.
	 */
	default void keyReleased(KeyEvent event) {
	}

	/**
	 * What to do when a mapped KeyBinding is released.
	 */
	default void keyReleased(KeyBinding key) {
	}

	/**
	 * What to do when any key is typed.
	 */
	default void keyTyped(char key) {
	}

	/**
	 * What to do when the mouse wheel is scrolled.
	 */
	default void mouseWheel(int amount) {
	}
}
