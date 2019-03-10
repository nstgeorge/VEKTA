package vekta.context;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;

import static vekta.Vekta.*;

/**
 * Pause inject implementation as a Context. Eventually this can be converted over to use a Menu for additional flexibility.
 */
public class PauseMenuContext implements Context {
	private final World world;

	// Pause inject options
	private final String[] options = {"Continue", "Restart", "Quit to Menu"};
	private int selected = 0;

	public PauseMenuContext(World world) {
		this.world = world;
	}

	@Override
	public void focus() {
	}

	@Override
	public void render() {
		// Border box
		v.rectMode(CORNER);
		v.stroke(UI_COLOR);
		v.fill(0);
		v.rect(-1, -1, v.width / 4F, v.height + 2);
		// Logo
		v.shapeMode(CENTER);
		v.shape(Resources.logo, v.width / 8F, 100, (v.width / 4F) - 100, ((v.width / 4F) - 100) / 3.392F);
		// Options
		for(int i = 0; i < options.length; i++) {
			drawOption(options[i], (v.height / 2) + (i * 100), i == selected);
		}
		v.textFont(bodyFont);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to select", v.width / 8F, (v.height / 2) + (options.length * 100) + 100);
		v.hint(ENABLE_DEPTH_TEST);
		//noLoop();
	}

	/**
	 * Draws an option of name "name" at yPos in the overlay
	 */
	private void drawOption(String name, int yPos, boolean selected) {
		// Shape ---------------------
//		v.hint(DISABLE_DEPTH_TEST);
		//		v.camera();
		//		v.noLights();
		if(selected)
			v.stroke(255);
		else
			v.stroke(UI_COLOR);
		v.fill(1);
		v.rectMode(CENTER);
		v.rect(v.width / 8F, yPos, 200 + (selected ? 10 : 0), 50);
		// Text ----------------------
		v.textFont(bodyFont);
		v.stroke(0);
		v.fill(UI_COLOR);
		v.textAlign(CENTER, CENTER);
		v.text(name, v.width / 8F, yPos - 3);
	}

	@Override
	public void keyPressed(KeyBinding key) {
		if(key == KeyBinding.MENU_CLOSE) {
			setContext(world);
		}
		else if(key == KeyBinding.MENU_UP) {
			// Play the sound for changing inject selection
			Resources.playSound("change");
			selected = Math.max(selected - 1, 0);
		}
		else if(key == KeyBinding.MENU_DOWN) {
			// Play the sound for changing inject selection
			Resources.playSound("change");
			selected = Math.min(selected + 1, options.length - 1);
		}
		else if(key == KeyBinding.MENU_SELECT) {
			//			Resources.stopMusic("theme");
			Resources.playSound("select");
			switch(selected) {
			case (0):
				setContext(world);
				break;
			case (1):
				world.restart();
				break;
			case (2):
				setContext(mainMenu);
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyBinding key) {
	}

	@Override
	public void mouseWheel(int amount) {
		selected = max(0, min(options.length - 1, selected + amount));
	}
}
