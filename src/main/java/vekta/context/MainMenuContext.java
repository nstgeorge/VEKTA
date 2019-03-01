package vekta.context;

import processing.core.PVector;
import processing.data.IntDict;
import processing.data.StringDict;
import vekta.*;

import static vekta.Vekta.*;

public class MainMenuContext implements Context {
	String[] modes = {"Singleplayer"};
	int selectedMode;
	Hyperspace hyperspace;
	// Settings
	boolean inSettings;
	StringDict settingsOptions;
	IntDict settingsDefinitions;
	int[] selectedOptions;
	int selectedSetting;

	final int SETTINGS_SPACING = 50;

	public MainMenuContext() {
		Vekta v = getInstance();
		Resources.setMusic("theme");
		hyperspace = new Hyperspace(new PVector(v.width / 2F, v.height / 2F - 100), 0.1F, 170);
		inSettings = false;
		settingsOptions = new StringDict();
		settingsOptions.set("Music", "On,Off");
		settingsOptions.set("Sound", "On,Off");
		settingsDefinitions = new IntDict();
		settingsDefinitions.set("On", 1);
		settingsDefinitions.set("Off", 0);
		selectedOptions = new int[settingsOptions.size()];
	}

	@Override
	public void render() {
		Vekta v = getInstance();
		// hyperspace background
		v.camera(v.width / 2F, v.height / 2F, (v.height / 2F) / tan(PI * 30F / 180F), v.width / 2F, v.height / 2F, 0F,
				0F, 1F, 0F);
		v.background(0);
		hyperspace.render();

		if(!inSettings) {
			drawMain();
		}
		else {
			drawSettings();
		}
	}

	private void drawMain() {
		Vekta v = getInstance();
		v.hint(DISABLE_DEPTH_TEST);
		v.camera();
		v.noLights();
		v.shapeMode(CENTER);
		v.shape(Resources.logo, v.width / 2F, v.height / 4F, 339.26F, 100);
		for(int i = 0; i < modes.length; i++) {
			drawButton(modes[i], (v.height / 2) + (i * 100), i == selectedMode);
		}
		drawButton("Settings", (v.height / 2) + (modes.length * 100), selectedMode == modes.length);
		drawButton("Quit", (v.height / 2) + (modes.length * 100) + 100, selectedMode == modes.length + 1);

		v.textFont(bodyFont);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.text("X to select", v.width / 2F, (v.height / 2) + (modes.length * 100) + 200);

		v.textSize(14);
		v.text("Created by Nate St. George", v.width / 2F, (v.height / 2) + (modes.length * 100) + 300);
		v.hint(ENABLE_DEPTH_TEST);
	}

	private void drawSettings() {
		Vekta v = getInstance();
		v.hint(DISABLE_DEPTH_TEST);
		v.camera();
		v.noLights();
		v.textFont(bodyFont);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.textSize(30);
		v.text("Settings", v.width / 2F, 100);
		v.textFont(bodyFont);
		// Draw each option
		int optionIndex = 0;
		for(String key : settingsOptions.keyArray()) {
			String[] options = settingsOptions.get(key).split(",");
			if(selectedSetting == optionIndex)
				v.fill(UI_COLOR);
			v.text(key, 500, 200 + (optionIndex * SETTINGS_SPACING));
			for(String option : options) {
				if(Settings.get(key.toLowerCase()) == settingsDefinitions.get(option)) {
					v.text(option, v.width - 500, 200 + (optionIndex * SETTINGS_SPACING));
					selectedOptions[optionIndex] = settingsDefinitions.get(option);
				}
			}
			v.fill(255, 255, 255);
			optionIndex++;
		}
		drawButton("Back", 200 + ((optionIndex + 1) * SETTINGS_SPACING), selectedSetting == settingsOptions.size());
		v.textSize(16);
		v.fill(255, 255, 255);
		v.text("X to cycle options, ESC to go back", v.width / 2F, 200 + ((optionIndex + 2) * SETTINGS_SPACING));
		v.textFont(bodyFont);
		v.hint(ENABLE_DEPTH_TEST);
	}

	private void drawButton(String name, int yPos, boolean selected) {
		Vekta v = getInstance();
		if(selected)
			v.stroke(255);
		else
			v.stroke(UI_COLOR);
		v.fill(1);
		v.rectMode(CENTER);
		v.rect(v.width / 2F, yPos, 200, 50);
		// Text ----------------------
		v.textFont(bodyFont);
		v.stroke(0);
		v.fill(UI_COLOR);
		v.textAlign(CENTER, CENTER);
		v.text(name, v.width / 2F, yPos - 3);
	}

	void updateSetting() {
		selectedOptions[selectedSetting] = (selectedOptions[selectedSetting] + 1) % settingsOptions.valueArray()[selectedSetting].split(",").length;
		Settings.set(settingsOptions.keyArray()[selectedSetting].toLowerCase(), selectedOptions[selectedSetting]);
		// Quick! Turn down the music if the player wants it gone!
		Resources.updateMusicVolume();
	}

	@Override
	public void keyPressed(char key) {
		Vekta v = getInstance();
		if(key == ESC) {
			inSettings = false;
		}
		if(key == 'w') {
			// Play the sound for changing menu selection
			Resources.playSound("change");
			if(!inSettings) {
				selectedMode = Math.max(selectedMode - 1, 0);
				v.redraw();
			}
			else {
				selectedSetting = Math.max(selectedSetting - 1, 0);
				v.redraw();
			}
		}
		if(key == 's') {
			// Play the sound for changing menu selection
			Resources.playSound("change");
			if(!inSettings) {
				selectedMode = Math.min(selectedMode + 1, modes.length + 1);
				v.redraw();
			}
			else {
				selectedSetting = Math.min(selectedSetting + 1, settingsOptions.size());
				v.redraw();
			}
		}
		if(key == 'x') {
			// Play the sound for selection
			Resources.playSound("change");
			if(!inSettings) {
				// Just selected a game mode
				if(selectedMode < modes.length) {
					selectMode();
				}
				else {
					// Just selected settings
					if(selectedMode == modes.length) {
						inSettings = true;
					}
					//Just selected quit
					if(selectedMode == modes.length + 1) {
						v.exit();
					}
				}
			}
			else {
				if(selectedSetting == settingsOptions.size()) {
					// Settings autosave
					inSettings = false;
				}
				else {
					// Update settings
					updateSetting();
				}
			}
		}
	}

	private void selectMode() {
		if(selectedMode == 0)
			startWorld(new Singleplayer());
		//if(selectedMode == 1) startGamemode(new Multiplayer());
//		if(getSetting("music") > 0)
//			theme.stop();
	}

	@Override
	public void keyReleased(char key) {
	}

	@Override
	public void mouseWheel(int amount) {
	}
}  
