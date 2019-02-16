import java.util.*;
import processing.sound.*;

final String FONTNAME = "font/stm.ttf";
final int MAX_DISTANCE = 2000; // Maximum distance for calculating influence vector
Gamemode game;
Menu menu;

// Settings
int[] settings = {
  0, // Sound (0 = off, 10 = full volume)
  0  // Music (0 = off, 10 = full volume)
};

// y u no enums
final int SETTINGS_SOUND = 1;
final int SETTINGS_MUSIC = 1;

// Game-balancing variables and visual settings

final double G = 6.674*Math.pow(10,-11);
public final double SCALE = 3 * Math.pow(10, 8);
final float VECTOR_SCALE = 5;
final int MAX_PLANETS = 100;
final int TRAIL_LENGTH = 10;
final float DEF_ZOOM = (height/2.0) / tan(PI*30.0 / 180.0); // For some reason, this is the default eyeZ location for Processing

// Pause menu options
String[] pauseMenu = {"Continue" , "Restart", "Quit to Menu"};
int pauseSelected = 0;

// Fonts
PFont headerFont;
PFont bodyFont;

// HUD/Menu overlay
PGraphics overlay;

// A sick logo
PShape logo;

// Menu variables + multiplayer scorekeeping
boolean modePicked = false;
boolean switchedToGame = false;
boolean paused = false;
int selectedMode = 0;
int[] playerWins = new int[2];

// Sounds
SoundFile theme;
SoundFile laser;
SoundFile death;
SoundFile engine;
SoundFile change;
SoundFile select;

// ???
BrownNoise noise;

void setup() {
  // Important visual stuff
  fullScreen(P3D);
  background(0);
  frameRate(60);
  noCursor();
  textMode(SHAPE);
  // Overlay initialization
  overlay = createGraphics(width, height);
  // Fonts
  headerFont = createFont(FONTNAME, 72);
  bodyFont = createFont(FONTNAME, 24);
  // Images
  logo = loadShape("VEKTA.svg");
  // All sounds and music. These must be instantiated in the main file
  // Music (temporarily disabled)
  //theme = new SoundFile(this, "main.wav");
  
  //// Sound (temporarily disabled)
  laser = new SoundFile(this, "laser.wav");
  death = new SoundFile(this, "death.wav");
  engine = new SoundFile(this, "engine.wav");
  change = new SoundFile(this, "change.wav");
  select = new SoundFile(this, "select.wav");
  
  playerWins[0] = 0;
  playerWins[1] = 0;
  menu = new Menu();
}  

void draw() {
  // Menu / game render switch
  if(modePicked) {
    if(selectedMode == 2) exit();
    else if(!switchedToGame) {
      if(selectedMode == 0) game = new Singleplayer();
      //if(selectedMode == 1) game = new Multiplayer();
      if(settings[SETTINGS_MUSIC] > 0) theme.stop();
      game.init();
      switchedToGame = true;
    }
  }
  if(modePicked && switchedToGame) {
    game.render();
  } else {
    menu.render();
  }
  
  // Pause menu overlay
  hint(DISABLE_DEPTH_TEST);
  camera();
  noLights();
  // FPS OVERLAY
  textAlign(LEFT);
  text("FPS = " + frameRate, 50, height - 50);
  if(paused) {
    hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();
    // Border box
    rectMode(CORNER);
    stroke(0, 255, 0);
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
    //redraw();
  } else {
    loop();
  }
}

void keyPressed() {
  // Toggle pause
  if(key == ESC) {
    key = 0;
    paused = !paused;
  }
  // Pause controls 
  else if(paused) {
    if(key == 'w') {
      // Play the sound for changing menu selection
      if(settings[SETTINGS_SOUND] > 0) change.play();
      pauseSelected = Math.max(pauseSelected - 1, 0);
    } 
    if(key == 's') {
      // Play the sound for changing menu selection
      if(settings[SETTINGS_SOUND] > 0) change.play();
      pauseSelected = Math.min(pauseSelected + 1, pauseMenu.length - 1);
    }
    if(key == 'x') {
      if(settings[SETTINGS_MUSIC] > 0) theme.stop();
      // Play the sound for selection
      if(settings[SETTINGS_SOUND] > 0) select.play();
      switch(pauseSelected) {
        case(0):
          clearOverlay();
          paused = false;
          loop();
          break;
        case(1):
          game.init();
          clearOverlay();
          paused = false;
          break;
        case(2):
          clearOverlay();
          modePicked = false;
          switchedToGame = false;
          paused = false;
          break;
        default:
          break;
      }
    }
    redraw();
  } 
  // Send event to game
  else if(modePicked) {
    game.keyPressed(key);
  }
  // Send event to menu
  else {
    menu.keyPressed(key);
  }
}

void keyReleased () {
  if(modePicked) {
    game.keyReleased(key);
  }
}  

void clearOverlay() {
  if(overlay.isLoaded()) {
    overlay.clear();
    overlay.beginDraw();
    overlay.background(0, 0);
    overlay.endDraw();
    overlay.setLoaded(false);
  }
}  

void drawOverlay() {
  // Overlay the overlay
  // NOTE: THIS IS VERY SLOW. Use only for menus, not gameplay!
  if(overlay.isLoaded()) {
    overlay.loadPixels();
    loadPixels();
    for(int i = 0; i < pixels.length; i++)
      if(overlay.pixels[i] != color(0)) pixels[i] = overlay.pixels[i];
    updatePixels();
    overlay.updatePixels();
    //image(overlay, 0, 0);
    //redraw();
  } 
}  

  /**
    Draws an option of name "name" at yPos in the overlay
  */
 private void drawOption(String name, int yPos, boolean selected) {
  // Shape ---------------------
  hint(DISABLE_DEPTH_TEST);
  camera();
  noLights();
  if(selected) stroke(255);
  else stroke(0, 255, 0);
  fill(1);
  rectMode(CENTER);
  rect(width / 8, yPos, 200, 50);
  // Text ----------------------
  textFont(bodyFont);
  stroke(0);
  fill(0, 255, 0);
  textAlign(CENTER, CENTER);
  text(name, width / 8, yPos - 3);
}  
