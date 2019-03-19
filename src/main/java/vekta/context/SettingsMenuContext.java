package vekta.context;

import processing.data.IntDict;
import processing.data.StringDict;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;
import vekta.menu.handle.MainMenuHandle;

import static vekta.Vekta.*;

public class SettingsMenuContext implements Context {
	private final Context parent;

	// Settings
	private final transient StringDict settingsOptions = new StringDict();
	private final transient IntDict settingsDefinitions = new IntDict();
	private final int[] selectedOptions;
	private int selectedSetting;

	final int SETTINGS_SPACING = 50;

	public SettingsMenuContext(Context parent) {
		this.parent = parent;

		settingsOptions.set("Music", "On,Off");
		settingsOptions.set("Sound", "On,Off");
		settingsDefinitions.set("On", 1);
		settingsDefinitions.set("Off", 0);
		selectedOptions = new int[settingsOptions.size()];
	}

	@Override
	public void focus() {
	}

	@Override
	public void render() {
		// hyperspace background
		v.camera(v.width / 2F, v.height / 2F, (v.height / 2F) / tan(PI * 30F / 180F), v.width / 2F, v.height / 2F, 0F,
				0F, 1F, 0F);
		v.background(0);
		MainMenuHandle.HYPERSPACE.render();

		// Draw settings
		//		v.hint(DISABLE_DEPTH_TEST);
		//		v.camera();
		//		v.noLights();
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
				if(Settings.getInt(key.toLowerCase()) == settingsDefinitions.get(option)) {
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
		v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to cycle options, " + Settings.getKeyText(KeyBinding.MENU_CLOSE) + " to go back", v.width / 2F, 200 + ((optionIndex + 2) * SETTINGS_SPACING));
	}

	private void drawButton(String name, int yPos, boolean selected) {
		if(selected)
			v.stroke(255);
		else
			v.stroke(name.equals("Back") ? 100 : UI_COLOR);
		v.fill(1);
		v.rectMode(CENTER);
		v.rect(v.width / 2F, yPos, 200 + (selected ? 10 : 0), 50);
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
		Resources.adjustFromSettings();
	}

	@Override
	public void keyPressed(KeyBinding key) {
		if(key == KeyBinding.MENU_CLOSE) {
			setContext(parent);
		}
		if(key == KeyBinding.MENU_UP) {
			// Play the sound for changing inject selection
			Resources.playSound("change");
			selectedSetting = Math.max(selectedSetting - 1, 0);
			v.redraw();
		}
		if(key == KeyBinding.MENU_DOWN) {
			// Play the sound for changing inject selection
			Resources.playSound("change");
			selectedSetting = Math.min(selectedSetting + 1, settingsOptions.size());
			v.redraw();
		}
		if(key == KeyBinding.MENU_SELECT) {
			// Play the sound for selection
			Resources.playSound("change");
			if(selectedSetting == settingsOptions.size()) {
				// Return to parent context
				setContext(parent);
			}
			else {
				// Update settings
				updateSetting();
			}
		}
	}

	@Override
	public void keyReleased(KeyBinding key) {
	}

	@Override
	public void mouseWheel(int amount) {
	}
}  
