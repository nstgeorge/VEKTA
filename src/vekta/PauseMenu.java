package vekta;

import static vekta.Vekta.*;

/**
  Pause menu implementation as a Context. Eventually this can be converted over to use a Menu for additional flexibility.
*/
class PauseMenu implements Context {
  private final World world;
  
  // Pause menu options
  private final String[] pauseMenu = {"Continue" , "Restart", "Quit to Menu"};
  private int selected = 0;
  
  public PauseMenu(World world) {
    this.world = world;
  }
  
  @Override
  void render() {
    Vekta v = Vekta.getInstance();
    
    // Border box
    v.rectMode(CORNER);
    v.stroke(UI_COLOR);
    v.fill(0);
    v.rect(-1, -1, v.width / 4, v.height + 2);
    // Logo
    v.shapeMode(CENTER);
    v.shape(logo, width / 8, 100, (width / 4) - 100, ((width / 4) - 100) / 3.392);
    // Options
    for(int i = 0; i < pauseMenu.length; i++) {
      drawOption(pauseMenu[i], (height / 2) + (i * 100), i == selected);
    }
    textFont(bodyFont);
    stroke(0);
    fill(255);
    textAlign(CENTER);
    text("X to select", width / 8, (height / 2) + (pauseMenu.length * 100) + 100);
    hint(ENABLE_DEPTH_TEST);
    //noLoop();
  }
  
  @Override
  void keyPressed(char key) {
    if(key == ESC) {
      unpause();
    }
    else if(key == 'w') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      selected = Math.max(selected - 1, 0);
    } 
    else if(key == 's') {
      // Play the sound for changing menu selection
      if(getSetting("sound") > 0) change.play();
      selected = Math.min(selected + 1, pauseMenu.length - 1);
    }
    else if(key == 'x') {
      if(getSetting("music") > 0) theme.stop();
      // Play the sound for selection
      if(getSetting("sound") > 0) select.play();
      clearOverlay();
      switch(selected) {
        case(0):
          setContext(world);
          break;
        case(1):
          world.restart();
          break;
        case(2):
          setContext(mainMenu);
          break;
      }
    }
  }
  
  @Override
  public void keyReleased(char key) {}
  
  @Override
  public void mouseWheel(int amount) {}
}
