class Menu {
  String[] modes = {"Singleplayer"};
  Hyperspace hyperspace;
  // Settings
  boolean    inSettings;
  StringDict settingsOptions;
  IntDict    settingsDefinitions;
  int[]      selectedOptions;
  int        selectedSetting;
  
  final int  SETTINGS_SPACING = 50;
  
  public Menu() {
    theme.amp(getSetting("music"));
    theme.play();
    hyperspace = new Hyperspace(new PVector(width/2, height/2 - 100), 0.1, 170);
    inSettings = false;
    settingsOptions = new StringDict();
    settingsOptions.set("Music", "On,Off");
    settingsOptions.set("Sound", "On,Off");
    settingsDefinitions = new IntDict();
    settingsDefinitions.set("On", 1);
    settingsDefinitions.set("Off", 0);
    selectedOptions = new int[settingsOptions.size()];
  }  
  
  void render() {
    // hyperspace background
    camera(width/2, height/2, (height/2.0) / tan(PI*30.0 / 180.0), width/2, height/2, 0.0, 
         0.0, 1.0, 0.0);
    background(0);
    hyperspace.render();
    
    if(!inSettings) {
      drawMain();
    } else {
      drawSettings();
    }
  }
  
  private void drawMain() {
    hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();
    shapeMode(CENTER);
    shape(logo, width/2, height/4, 339.26, 100);
    for(int i = 0; i < modes.length; i++) {
      drawButton(modes[i], (height / 2) + (i * 100), i == selectedMode);
    }
    drawButton("Settings", (height / 2) + (modes.length * 100), selectedMode == modes.length);
    drawButton("Quit", (height / 2) + (modes.length * 100) + 100, selectedMode == modes.length + 1);
    
    textFont(bodyFont);
    stroke(0);
    fill(255);
    textAlign(CENTER);
    text("X to select", width / 2, (height / 2) + (modes.length * 100) + 200);
    
    textSize(14);
    text("Created by Nate St. George", width / 2, (height / 2) + (modes.length * 100) + 300);
    hint(ENABLE_DEPTH_TEST);
  }
  
  private void drawSettings() {
    hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();
    textFont(bodyFont);
    stroke(0);
    fill(255);
    textAlign(CENTER);
    textSize(30);
    text("Settings", width / 2, 100);
    textFont(bodyFont);
    // Draw each option
    int optionIndex = 0;
    for(String key : settingsOptions.keyArray()) {
      String[] options = settingsOptions.get(key).split(",");
      if(selectedSetting == optionIndex) fill(0, 255, 0);
      text(key, 500, 200 + (optionIndex * SETTINGS_SPACING));
      for(String option : options) {
        if(getSetting(key.toLowerCase()) == settingsDefinitions.get(option)) {
          text(option, width - 500, 200 + (optionIndex * SETTINGS_SPACING));
          selectedOptions[optionIndex] = settingsDefinitions.get(option);
        }
      }
      fill(255, 255, 255);
      optionIndex++;
    }
    drawButton("Save", 200 + ((optionIndex + 1) * SETTINGS_SPACING), selectedSetting == settingsOptions.size());
    textSize(16);
    fill(255, 255, 255);
    text("X to cycle options, ESC to go back", width / 2, 200 + ((optionIndex + 2) * SETTINGS_SPACING));
    textFont(bodyFont);
    hint(ENABLE_DEPTH_TEST);
  }
  
  private void drawButton(String name, int yPos, boolean selected) {
    if(selected) stroke(255);
    else stroke(0, 255, 0);
    fill(1);
    rectMode(CENTER);
    rect(width / 2, yPos, 200, 50);
    // Text ----------------------
    textFont(bodyFont);
    stroke(0);
    fill(0, 255, 0);
    textAlign(CENTER, CENTER);
    text(name, width / 2, yPos - 3);
  }  
  
  void updateSetting() {
    selectedOptions[selectedSetting] = (selectedOptions[selectedSetting] + 1) % settingsOptions.valueArray()[selectedSetting].split(",").length;
    setSetting(settingsOptions.keyArray()[selectedSetting].toLowerCase(), selectedOptions[selectedSetting]);
    // Quick! Turn down the music if the player wants it gone!
    theme.amp(getSetting("music"));
  }
  
  void keyPressed(char key) {
    if (key == ESC) {
      inSettings = false;
    }
    if(key == 'w') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      if(!inSettings) {
        selectedMode = Math.max(selectedMode - 1, 0);
        redraw();
      } else {
        selectedSetting = Math.max(selectedSetting - 1, 0);
        redraw();
      }
    } 
    if(key == 's') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      if(!inSettings) {
        selectedMode = Math.min(selectedMode + 1, modes.length + 1);
        redraw();
      } else {
        selectedSetting = Math.min(selectedSetting + 1, settingsOptions.size());
        redraw();
      }
    }
    if(key == 'x') {
      // Play the sound for selection
      if(getSetting("sound") > 0) select.play();
      if(!inSettings) {
        // Just selected a game mode
        if(selectedMode < modes.length) {
          modePicked = true;
        } else {
          // Just selected settings
          if(selectedMode == modes.length) {
            inSettings = true;
          }
          //Just selected quit
          if(selectedMode == modes.length + 1) {
            exit();
          }
        }
      } else {
        if(selectedSetting == settingsOptions.size()) {
          // Save the settings
          saveSettings();
        } else {
          // Update settings
          updateSetting();
        }
      }
    }
  }  
}  
