package vekta;

import static vekta.Vekta.*;

/**
 * Pause menu implementation as a Context. Eventually this can be converted over to use a Menu for additional flexibility.
 */
class PauseMenu implements Context {
	private final World world;

	// Pause menu options
	private final String[] pauseMenu = {"Continue", "Restart", "Quit to Menu"};
	private int selected = 0;

	public PauseMenu(World world) {
		this.world = world;
	}

	@Override
	public void render() {
		Vekta v = Vekta.getInstance();

		// Border box
		v.rectMode(CORNER);
		v.stroke(UI_COLOR);
		v.fill(0);
		v.rect(-1, -1, v.width / 4F, v.height + 2);
		// Logo
		v.shapeMode(CENTER);
		v.shape(logo, v.width / 8F, 100, (v.width / 4F) - 100, ((v.width / 4F) - 100) / 3.392F);
		// Options
		for(int i = 0; i < pauseMenu.length; i++) {
			drawOption(pauseMenu[i], (v.height / 2) + (i * 100), i == selected);
		}
		v.textFont(bodyFont);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.text("X to select", v.width / 8F, (v.height / 2) + (pauseMenu.length * 100) + 100);
		v.hint(ENABLE_DEPTH_TEST);
		//noLoop();
	}

	/**
	 * Draws an option of name "name" at yPos in the overlay
	 */
	private void drawOption(String name, int yPos, boolean selected) {
		Vekta v = getInstance();
		// Shape ---------------------
		v.hint(DISABLE_DEPTH_TEST);
		v.camera();
		v.noLights();
		if(selected)
			v.stroke(255);
		else
			v.stroke(UI_COLOR);
		v.fill(1);
		v.rectMode(CENTER);
		v.rect(v.width / 8F, yPos, 200, 50);
		// Text ----------------------
		v.textFont(bodyFont);
		v.stroke(0);
		v.fill(UI_COLOR);
		v.textAlign(CENTER, CENTER);
		v.text(name, v.width / 8F, yPos - 3);
	}

	@Override
	public void keyPressed(char key) {
		if(key == ESC) {
			clearOverlay();
			setContext(world);
		}
		else if(key == 'w') {
			// Play the sound for changing menu selection
			if(getSetting("sound") > 0)
				change.play();
			selected = Math.max(selected - 1, 0);
		}
		else if(key == 's') {
			// Play the sound for changing menu selection
			if(getSetting("sound") > 0)
				change.play();
			selected = Math.min(selected + 1, pauseMenu.length - 1);
		}
		else if(key == 'x') {
			if(getSetting("music") > 0)
				theme.stop();
			// Play the sound for selection
			if(getSetting("sound") > 0)
				select.play();
			clearOverlay();
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
	public void keyReleased(char key) {
	}

	@Override
	public void mouseWheel(int amount) {
	}
}
