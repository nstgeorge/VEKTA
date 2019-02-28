interface Context {
  /**
    Draws the context each loop
  */
  void render();
  
  /**
    What to do when a key is pressed
  */
  void keyPressed(char key);
  
  /**
    What to do when a key is released
  */
  void keyReleased(char key);
  
  /**
    What to do when the mouse wheel is scrolled
  */
  void mouseWheel(int amount);
}

/**
  Pause menu implementation as a context. Eventually this can be converted over to use a Menu for additional flexibility.
*/
class PausedContext implements Context {
  private final Context parent;
  
  // Pause menu options
  String[] pauseMenu = {"Continue" , "Restart", "Quit to Menu"};
  int pauseSelected = 0;
  
  public PausedContext(Context parent) {
    this.parent = parent;
  }
  
  @Override
  void render() {
    // TODO: render parent context below
    
    /*hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();*/
    // Border box
    rectMode(CORNER);
    stroke(UI_COLOR);
    fill(0);
    rect(-1, -1, width / 4, height + 2);
    // Logo
    shapeMode(CENTER);
    shape(logo, width / 8, 100, (width / 4) - 100, ((width / 4) - 100) / 3.392);
    // Options
    for(int i = 0; i < pauseMenu.length; i++) {
      drawOption(pauseMenu[i], (height / 2) + (i * 100), i == pauseSelected);
    }
    textFont(bodyFont);
    stroke(0);
    fill(255);
    textAlign(CENTER);
    text("X to select", width / 8, (height / 2) + (pauseMenu.length * 100) + 100);
    hint(ENABLE_DEPTH_TEST);
    noLoop();
  }
  
  @Override
  void keyPressed(char key) {
    if(key == ESC) {
      unpause();
    }
    if(key == 'w') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      pauseSelected = Math.max(pauseSelected - 1, 0);
    } 
    if(key == 's') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      pauseSelected = Math.min(pauseSelected + 1, pauseMenu.length - 1);
    }
    if(key == 'x') {
      if(getSetting("music") > 0) theme.stop();
      // Play the sound for selection
      if(getSetting("sound") > 0) select.play();
      switch(pauseSelected) {
        case(0):
          clearOverlay();
          loop();
          break;
        case(1):
          game.restart();
          break;
        case(2):
          clearOverlay();
          modePicked = false;
          switchedToGame = false;
          break;
        default:
          break;
      }
      unpause();
    }
  }
  
  @Override
  public void keyReleased(char key) {}
  
  @Override
  public void mouseWheel(int amount) {}
  
  private void unpause() {
    paused = false;
    closeContext(this);
  }
}
