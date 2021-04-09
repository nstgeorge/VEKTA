package vekta.context;

import processing.core.PShape;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;
import vekta.player.Player;

import static vekta.Vekta.*;

/**
 * Pause menu implementation as a Context.
 */
// TODO: convert to PauseMenuHandle
public class PauseMenuContext implements Context {
	private static final String[] OPTIONS = {"Continue", "Restart"/*, "Settings"*/, "Quit to Menu"};

	private static final PShape LOGO = v.loadShape("vekta_wordmark.svg");

	private final Context parent;
	//	private final Player player;

	private int selected = 0;

	public PauseMenuContext(Context parent, Player player) {
		this.parent = parent;
		//		this.player = player;
	}

	@Override
	public void focus() {
	}

	@Override
	public void render() {
		// Border
		v.rectMode(CORNER);
		v.stroke(UI_COLOR);
		v.fill(0);
		v.rect(-1, -1, v.width / 4F, v.height + 2);

		// Logo
		v.shapeMode(CENTER);
		v.shape(LOGO, v.width / 8F, 100, 484.6125F, 75);

		// Options
		for(int i = 0; i < OPTIONS.length; i++) {
			drawOption(OPTIONS[i], (v.height / 2) + (i * 100), i == selected);
		}

		// Helper text
		v.textFont(BODY_FONT);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to select", v.width / 8F, (v.height / 2F) + (OPTIONS.length * 100) + 100);
	}

	private void drawOption(String name, int yPos, boolean selected) {
		if(selected) {
			v.stroke(255);
		}
		else {
			v.stroke(name.equals(OPTIONS[OPTIONS.length - 1]) ? 100 : UI_COLOR);
		}
		v.fill(BUTTON_COLOR);
		v.rectMode(CENTER);
		v.strokeWeight(selected ? 2 : 1);
		v.rect(v.width / 8F, yPos, 200 + (selected ? 10 : 0), 50);
		v.strokeWeight(1);
		// Text ----------------------
		v.textFont(BODY_FONT);
		v.stroke(0);
		v.fill(UI_COLOR);
		v.textAlign(CENTER, CENTER);
		v.text(name, v.width / 8F, yPos - 3);
	}

	@Override
	public void keyPressed(KeyBinding key) {
		if(key == KeyBinding.MENU_CLOSE) {
			setContext(parent);
		}
		else if(key == KeyBinding.MENU_UP) {
			// Play the sound for changing inject selection
			Resources.playSound("change");
			selected = Math.max(selected - 1, 0);
		}
		else if(key == KeyBinding.MENU_DOWN) {
			// Play the sound for changing inject selection
			Resources.playSound("change");
			selected = Math.min(selected + 1, OPTIONS.length - 1);
		}
		else if(key == KeyBinding.MENU_SELECT) {
			//			Resources.stopMusic("theme");
			Resources.playSound("select");
			switch(selected) {
			case 0:
				setContext(getWorld());
				break;
			case 1:
				getWorld().restart();
				break;
			//			case 2:
			//				Menu menu = new Menu(player, new BackButton(parent), new MenuHandle());
			//				menu.select(new SettingsMenuButton());
			//				break;
			case 2/*3*/:
				getWorld().autosave();
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
		selected = max(0, min(OPTIONS.length - 1, selected + amount));
	}
}
