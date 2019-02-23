class Menu {
  String[] modes = {"Singleplayer"};
  Hyperspace hyperspace;
  
  public Menu() {
    if(getSetting("music") > 0) theme.play();
    hyperspace = new Hyperspace(new PVector(width/2, height/2 - 100), 0.1, 170);
    
  }  
  
  void render() {
    // hyperspace background
    camera(width/2, height/2, (height/2.0) / tan(PI*30.0 / 180.0), width/2, height/2, 0.0, 
         0.0, 1.0, 0.0);
    background(0);
    hyperspace.render();
    
    // Menu
    hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();
    shapeMode(CENTER);
    shape(logo, width/2, height/4, 339.26, 100);
    for(int i = 0; i < modes.length; i++) {
      drawOption(modes[i], (height / 2) + (i * 100), i == selectedMode);
    }
    drawOption("Quit", (height / 2) + (modes.length * 100), selectedMode == modes.length);
    
    textFont(bodyFont);
    stroke(0);
    fill(255);
    textAlign(CENTER);
    text("X to select", width / 2, (height / 2) + (modes.length * 100) + 100);
    
    textSize(14);
    text("Created by Nate St. George", width / 2, (height / 2) + (modes.length * 100) + 150);
    hint(ENABLE_DEPTH_TEST);
  }
  
  private void drawOption(String name, int yPos, boolean selected) {
    // Shape ---------------------
    //if(selected) overlay.stroke(255);
    //else overlay.stroke(0, 255, 0);
    //overlay.fill(1);
    //overlay.rectMode(CENTER);
    //overlay.rect(width / 2, yPos, 200, 50);
    //// Text ----------------------
    //overlay.textFont(bodyFont);
    //overlay.stroke(0);
    //overlay.fill(0, 255, 0);
    //overlay.textAlign(CENTER, CENTER);
    //overlay.text(name, width / 2, yPos - 3);
    
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
  
  void keyPressed(char key) {
    if(key == 'w') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      selectedMode = Math.max(selectedMode - 1, 0);
      redraw();
    } 
    if(key == 's') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      selectedMode = Math.min(selectedMode + 1, modes.length);
      redraw();
    }
    if(key == 'x') {
      // Play the sound for selection
      if(getSetting("sound") > 0) select.play();
      modePicked = true;
    }
  }  
}  
