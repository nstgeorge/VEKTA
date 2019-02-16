import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 
import processing.sound.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class vekta extends PApplet {




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

final double G = 6.674f*Math.pow(10,-11);
public final double SCALE = 3 * Math.pow(10, 8);
final float VECTOR_SCALE = 5;
final int MAX_PLANETS = 100;
final int TRAIL_LENGTH = 10;
final float DEF_ZOOM = (height/2.0f) / tan(PI*30.0f / 180.0f); // For some reason, this is the default eyeZ location for Processing

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

public void setup() {
  // Important visual stuff
  
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

public void draw() {
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
    shape(logo, width / 8, 100, (width / 4) - 100, ((width / 4) - 100) / 3.392f);
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

public void keyPressed() {
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

public void keyReleased () {
  if(modePicked) {
    game.keyReleased(key);
  }
}  

public void clearOverlay() {
  if(overlay.isLoaded()) {
    overlay.clear();
    overlay.beginDraw();
    overlay.background(0, 0);
    overlay.endDraw();
    overlay.setLoaded(false);
  }
}  

public void drawOverlay() {
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

interface Gamemode {
  /**
    Initializes all required variables
  */
  public void init();
  
  /**
    Draws all the things
  */
  public void render();
  
  /**
    What to do when a key is pressed
  */
  public void keyPressed(char key);
  
  /**
    What to do when a key is released
  */
  public void keyReleased(char key);
}  
class Hyperspace {
  private PVector origin;
  private float accel;
  private Particle[] particles;
  private final float INIT_VELOCITY = .2f;
  
  public Hyperspace(PVector origin, float accel, int particleNum) {
      this.origin = origin;
      this.accel = accel;
      particles = new Particle[particleNum];
      // Generate a new set of particles all at once
      int i = 0;
      while(i < particleNum) {
        particles[i] = newParticle(new PVector(random(0, width), random(0, height)));
        i++;
      }  
  }
  
  public Particle newParticle(PVector loc) {
    // Create a new random PVector
      PVector accelVector = loc.copy().sub(origin);
      accelVector.setMag(accel);
      return new Particle(loc.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
  }
  
  public Particle newParticle() {
    // Create a new random PVector
      PVector accelVector = PVector.random2D();
      accelVector.setMag(accel);
      return new Particle(origin.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
  }
  
  public void render() {
    update();
    for(Particle p : particles) {
      p.render();
    }
  }
  
  public void update() {
    for(int i = 0; i < particles.length; i++) {
      Particle p = particles[i];
      particles[i].update();
      if(p.getLoc().x > width + 300 || p.getLoc().y > height + 200 || p.getLoc().x < -300 || p.getLoc().y < -200) {
        particles[i] = newParticle();
      }
    }
  }
}  
class Menu {
  String[] modes = {"Singleplayer", "Multiplayer"};
  Hyperspace hyperspace;
  
  public Menu() {
    if(settings[SETTINGS_MUSIC] > 0) theme.play();
    hyperspace = new Hyperspace(new PVector(width/2, height/2 - 100), 0.1f, 170);
    
  }  
  
  public void render() {
    // hyperspace background
    camera(width/2, height/2, (height/2.0f) / tan(PI*30.0f / 180.0f), width/2, height/2, 0.0f, 
         0.0f, 1.0f, 0.0f);
    background(0);
    hyperspace.render();
    
    // Menu
    hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();
    shapeMode(CENTER);
    shape(logo, width/2, height/4, 339.26f, 100);
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
  
  public void keyPressed(char key) {
    if(key == 'w') {
      // Play the sound for changing menu selection
      if(settings[SETTINGS_SOUND] > 0) change.play();
      selectedMode = Math.max(selectedMode - 1, 0);
      redraw();
    } 
    if(key == 's') {
      // Play the sound for changing menu selection
      if(settings[SETTINGS_SOUND] > 0) change.play();
      selectedMode = Math.min(selectedMode + 1, modes.length);
      redraw();
    }
    if(key == 'x') {
      // Play the sound for selection
      if(settings[SETTINGS_SOUND] > 0) select.play();
      modePicked = true;
    }
  }  
}  

// This file is no longer useful, but before it's deleted, take out the camera equation, it works very well

/* import java.util.*;

class Multiplayer implements Gamemode {
  
  int planetCount = 0;
  boolean[] dead;
  boolean referenceFrameGone = false;
  PVector lastPosition;
  
  ArrayList<SpaceObject> objects = new ArrayList<SpaceObject>();
  ArrayList<Spaceship> ships = new ArrayList<Spaceship>();
  ArrayList<ArrayList<PVector>> oldPositions;
  
  UniverseGen generator = new UniverseGen(3000, 15);
  
  void init() {
    // Just making sure it's clear
    objects.clear();
    ships.clear();
    clearOverlay();
    frameCount = 0;
    referenceFrameGone = false;
    oldPositions = new ArrayList<ArrayList<PVector>>();
    
    objects = generator.generate();
    
    // Regular planet with highly eccentric orbit 
    //objects.add(new Planet(
    //  5.972 * Math.pow(10, 24), // Mass
    //  5,   // Radius
    //  width/2, height/3,  // Coords
    //  2, 3,  // Velocity
    //  color(255, 255, 255)
    //));
    
    //// Tiny lil bab planet
    //objects.add(new Planet(
    //  7.347 * Math.pow(10, 22), // Mass
    //  2,   // Radius
    //  width/2, (int)(height*0.75),  // Coords
    //  2, -2.7,  // Velocity
    //  color(0, 255, 255)
    //));
    
    //objects.add(new Planet(
    //  2.447 * Math.pow(10, 26), // Mass
    //  6,   // Radius
    //  width/4, (int)(height*0.75),  // Coords
    //  2, -2,  // Velocity
    //  color(255, 0, 100)
    //));
    
    //objects.add(new Planet(
    //  4.447 * Math.pow(10, 25), // Mass
    //  4,   // Radius
    //  (int)(width*.75), (int)(height*0.5),  // Coords
    //  -2, -3,  // Velocity
    //  color(100, 0, 255)
    //));
    
    //// Let's make a Sun!
    //objects.add(new Planet(
    //  1.989 * Math.pow(10, 30), // Mass
    //  15,   // Radius
    //  width/2, height/2,  // Coords
    //  0, 0,  // Velocity
    //  color(255, 255, 0)
    //));
    // P1
    ships.add(new Spaceship(
      "Player 1",  //Name
      5000,  // Mass
      5,     // Radius
      new PVector(1, 0), // Heading
      0, height / 2, // Position
      0, 0,    // Velocity
      color(0, 255, 0),
      0, 40, 50  // Control scheme, Speed, and Handling
    ));
    // P2
    ships.add(new Spaceship(
      "Player 2",  // Name
      5000,  // Mass
      5,     // Radius
      new PVector(-1, 0), // Heading
      width, height / 2, // Position
      0, 0,    // Velocity
      color(0, 0, 255),
      1, 40, 50  // Control scheme, Speed, and Handling
    ));
    
    dead = new boolean[ships.size()];
  }  
  
  void render() {
    background(0);
    SpaceObject markedForDeath = null;
    
    // If singleplayer, camera follow
    if(ships.size() == 1) {
      PVector p = ships.get(0).getPosition();
      float spd = ships.get(0).getVelocity().mag();
      camera(p.x, p.y, (.07*spd + .7) * (height/2.0) / tan(PI*30.0 / 180.0), p.x, p.y, 0.0, 
         0.0, 1.0, 0.0);
    } else {
      float longestDistance = 0;
      float totalX = 0;
      float totalY = 0;
      for(int i = 0; i < ships.size(); i++) {
        totalX += ships.get(i).getPosition().x;
        totalY += ships.get(i).getPosition().y;
      }  
      for(int i = 0; i < ships.size(); i++) {
        if(ships.get(i).getPosition().dist(new PVector(totalX / ships.size(), totalY / ships.size())) > longestDistance) {
          longestDistance = ships.get(i).getPosition().dist(new PVector(totalX / ships.size(), totalY / ships.size()));
          if(longestDistance > 3000) dead[i] = true;
        }
      }  
      camera(totalX / ships.size(), totalY / ships.size(), (Math.max(longestDistance - 100, height/2.0)) / tan((1.0/6.0) * PI), totalX / ships.size(), totalY / ships.size(), 0.0, 
         0.0, 1.0, 0.0);
    }  
    
    for(SpaceObject p : objects) {
      for(SpaceObject p2 : objects) {
        if(!(paused || dead[0] || dead[1])) p.getInfluenceVector(p2); // Get the influence vector of the object from every other object
        if(p.collidesWith(p2) && p != p2) {
          System.out.println(p + " has collided with " + p2);
          if(p.getMass() > p2.getMass()) markedForDeath = p2;
          else markedForDeath = p;
        };
      }
      for(Spaceship s : ships) {
        if(!(paused || dead[0] || dead[1]))s.getInfluenceVector(p);
        if(s.collidesWith(p)) {
          System.out.println(s + " has collided with " + p);
          // Assumes ship explodes on contact with anything
          markedForDeath = s;
        }
        for(Projectile projectile : s.getProjectiles()) {
          if(projectile != null) {
            if(!(paused || dead[0] || dead[1])) projectile.getInfluenceVector(p);
            if(projectile.collidesWith(p)) {
              markedForDeath = p;
               s.removeProjectile(projectile);
            }
            for(Spaceship s2 : ships) {
              if(projectile.collidesWith(s2) && s != s2) {
                markedForDeath = s2;
              }  
            }  
          }  
        } 
      } 
      if(!(paused || dead[0] || dead[1])) p.update();
      p.draw();
      drawTrail(p, planetCount);
      planetCount++;
    }
    int shipCount = objects.size();
    for(Spaceship s : ships) {
      if(!(paused || dead[0] || dead[1])) s.update();
      s.draw();
      drawTrail(s, shipCount);
      shipCount++;
      for(Projectile projectile : s.getProjectiles()) {
        if(projectile != null) {
          if(!(paused || dead[0] || dead[1])) projectile.update();
          projectile.draw();
        }  
      }
    }  
    if(markedForDeath != null) {
        System.out.println("Object " + markedForDeath + " is marked for death.");
        // I DIED!
        if(markedForDeath instanceof Spaceship) {
          dead[ships.indexOf(markedForDeath)] = true;
          engine.stop();
          death.play(); // Sound of death
          // oldPositions.remove(ships.indexOf(markedForDeath) + objects.size());
          lastPosition = markedForDeath.getPosition();
          ships.remove(markedForDeath);
          if(dead[0]) playerWins[1]++;
          if(dead[1]) playerWins[0]++;
        }  
        else oldPositions.remove(objects.indexOf(markedForDeath));
        objects.remove(markedForDeath);
        if(objects.size() == 0) {
          referenceFrameGone = true;
          lastPosition = ships.get(0).getPosition();
        }
      }  
    planetCount = 0;
    // Info
    if(!dead[0]) {
      int max = ships.get(0).MAX_PROJECTILES;
      for(Spaceship s : ships) {
        textFont(bodyFont);
        textAlign(CENTER);
        textSize(14);
        fill(0, 255, 0);
        text("Speed = " + (float)round(s.getVelocity().mag() * 100) / 100, s.getPosition().x, s.getPosition().y + 100);
        text("Ammo = " + (max - s.numProjectiles), s.getPosition().x, s.getPosition().y + 120);
      }
    }
    
    // Menus
    if((dead[0] == true || dead[1] == true) && modePicked) {
      overlay.beginDraw();
      overlay.background(0, 0);
      overlay.textFont(headerFont);
      overlay.textAlign(CENTER, CENTER);
      
      // Header text
      overlay.stroke(0);
      
      if(dead[0]) {
        overlay.fill(0, 0, 255);
        overlay.text("Player 2 wins!", width / 2, height / 2 - 100);
        
      }  
      if(dead[1]) {
        overlay.fill(0, 255, 0);
        overlay.text("Player 1 wins!", width / 2, height / 2 - 100);
      }
      
      // Wins per player
      overlay.textFont(bodyFont);
      overlay.fill(0, 255, 0);
      overlay.text(playerWins[0], width / 2 - 20, height / 2 - 50);
      overlay.fill(255);
      overlay.text(" - ", width / 2, height / 2 - 50);
      overlay.fill(0, 0, 255);
      overlay.text(playerWins[1], width / 2 + 20, height / 2 - 50);
      
      // Body text
      overlay.stroke(0);
      overlay.fill(255);
      overlay.textFont(bodyFont);
      overlay.text("X TO PLAY AGAIN", width / 2, height / 2 + 97);
      overlay.endDraw();
      overlay.setLoaded(true);
    }
    if(referenceFrameGone) {
      textFont(headerFont);
      textAlign(CENTER, CENTER);
      
      // Header text
      stroke(0);
      fill(255, 0, 0);
      text("You destroyed your inertial frame.", lastPosition.x, lastPosition.y - 100);
      
      // Body text
      stroke(0);
      fill(255);
      textFont(bodyFont);
      text("Basically you're dead. X TO RETRY", lastPosition.x, lastPosition.y + 97);
    }  
  }  
  
  void drawTrail(SpaceObject p, int planetCount) {
    if(frameCount > 1) {
      ArrayList<PVector> old = oldPositions.get(planetCount);
      if(old.size() > TRAIL_LENGTH) old.remove(0);
      old.add(new PVector(p.getPosition().x, p.getPosition().y));
      for(int i = 0; i < old.size() - 1; i++) {
        // Get two positions
       PVector oldPos = old.get(i);
       PVector newPos = old.get(i + 1);
       // Set the color and draw the line
       stroke(lerpColor(p.getColor(), color(0), (float)1 / (i + 1)));
       line(oldPos.x, oldPos.y,  newPos.x, newPos.y);
       // set oldPositions value
       oldPositions.set(planetCount, old);
      }  
    } else {
      // When the trails are just starting
      ArrayList<PVector> init = new ArrayList<PVector>();
      oldPositions.add(init);
      oldPositions.get(planetCount).add(new PVector(p.getPosition().x, p.getPosition().y));
    }  
  }  
  
  void keyPressed(char key) {
    if(dead[0] == true || dead[1] == true || referenceFrameGone) {
      if(key == 'x') {
        init();
      }
    } else {
     for(Spaceship s : ships) {
       s.keyPress(key);
     }
    } 
  }
  
  void keyReleased(char key) {
    for(Spaceship s : ships) {
      s.keyReleased(key);
    }
  }
}
*/
class Particle {
  private PVector loc;
  private PVector accel;
  private PVector velocity;
  private float dist;
  private float time;
  public Particle(PVector loc, PVector initVelocity, PVector accel) {
    this.loc = loc;
    this.accel = accel;
    dist = random(.2f, 2);
    this.velocity = initVelocity.mult(dist);
    time = 0;
  }
  
  public void render() {
    fill(0);
    stroke(Math.max((((time * 10) - 255) - (dist * 100)) / 4, 0));
    line(loc.x, loc.y, loc.x-(velocity.x * time/9), loc.y-(velocity.y * time/9));
  }
  
  public void update() {
    loc.add(velocity);
    velocity.add(accel);
    time++;
  }
  
  public PVector getLoc() {
    return loc;
  }  
}
 /**
*  Model for a planet.
*/
class Planet implements SpaceObject {
  // Default Planet settings
  private final double DEF_MASS = 1.989f * Math.pow(10, 30);
  private final int DEF_RADIUS = 15;
  private final PVector DEF_POSITION = new PVector(100, 100);
  private final PVector DEF_VELOCITY = new PVector(0,0);
  private final int DEF_COLOR = color(255, 255, 255);
  private String[] nameParts1 = {  // First parts of the name of a planet
    "Giga", "Atla", "Exo", "Zori", "Era", "Volta", "Dene", "Julu", "Poke", "Sala", "Huno", "Yeba", "Satu", "Plu", "Mercu", "Luki", "Pola", "Crato"
  };
  private String[] nameParts2 = {
    "dan", "san", "jor", "zed", "ranth", "ka", "", "th", "rn", "to", "krith", "n", "s", "sol", "deth", "rat", "kor", "k", "shyyyk", "tron", "don", "saur", "ris"
  };
  
  private String name;
  private int id;
  private double mass;
  private float radius;
  private PVector position;
  private PVector velocity;
  private int c;

  /**
  *  Default constructor for planets
  */
  public Planet() {
    mass     = DEF_MASS;
    radius   = DEF_RADIUS;
    position = DEF_POSITION;
    velocity = DEF_VELOCITY;
    c = DEF_COLOR;
  }
  
  public Planet(int id, double mass, float radius, int x, int y, float xVelocity, float yVelocity, int c) {
    
    this.name = nameParts1[(int)random(nameParts1.length)] + nameParts2[(int)random(nameParts2.length)];
    this.id = id;
    this.mass = mass;
    this.radius = radius;
    position = new PVector(x, y);
    velocity = new PVector(xVelocity, yVelocity);
    this.c = c;
  }
  
  // Draws the planet
  public void draw() {
    stroke(this.c);
    fill(0);
    ellipseMode(RADIUS);
    if(id == 1) System.out.println(velocity);
    ellipse(position.x, position.y, radius, radius);
  }
  
  public void update() {
    position.add(velocity);
  }  
  
  /**
    Calculates influence of this Planet *from* another object in space.
  */
  public PVector getInfluenceVector(ArrayList<SpaceObject> space) {
    
    for(int i = 0; i < space.size(); i++) {
      PVector influence = new PVector(0, 0);
      SpaceObject s = space.get(i);
      float dist = position.dist(s.getPosition());
      if(dist < MAX_DISTANCE) {
        double r = dist * SCALE;
        if(r == 0) return new PVector(0,0); // If the planet being checked is itself (or directly on top), don't move
        double force = G * ((mass * s.getMass()) / (r * r)); // G defined in orbit
        influence.add(new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag((float)(force / mass)));
      }
      velocity.add(influence);
    }
    return new PVector();
  }  
  
  /**
    Checks if this planet collides with another planet
  */
  public boolean collidesWith(SpaceObject s) {
    double r = PVector.dist(position, s.getPosition());
    if(r < (getRadius() + s.getRadius())) return true;
    return false;
  }  
  
  public @Override
  double getMass() {
    return mass;
  }
  
  public int getID() { return id; }
  
  public String getName() {
    return name;
  }  
  
  // Mass setter
  public void setMass(int mass) {
    this.mass = mass;
  }
  
  public @Override
  float getRadius() {
    return radius;
  }
  
  // Radius setter
  public void setRadius(int radius) {
    this.radius = radius;
  }
  
  public int getColor() {
    return c;
  }  
  
  public void setColor(int c) {
    this.c = c;
  }  
  
  public @Override
  PVector getPosition() {
    return position.copy();
  }
  
  public @Override
  PVector getVelocity() {
    return velocity.copy();
  }
  
  public @Override
  PVector addVelocity(PVector add) {
    return velocity.add(add);
  }
  
  /**
    Returns a PVector position that stays bounded to the screen.
  */
  private PVector setPlanetLoc(PVector position, PVector velocity) {
    if(position.x + velocity.x <= 0 && position.y + velocity.y <= 0)
      return new PVector(width, height);
      
     else if(position.x + velocity.x <= 0)
       return new PVector(width, (position.y + velocity.y) % height);
       
     else if(position.y +velocity.y <= 0)
       return new PVector((position.x + velocity.x) % width, height);
       
     else
       return new PVector((position.x + velocity.x) % width, (position.y + velocity.y) % height);
  }
}
class Projectile implements SpaceObject {
  // Default settings
  private final double DEF_MASS = 1000;
  private final int DEF_RADIUS = 2;
  private final int DEF_SPEED = 7;

  private SpaceObject parent;
  private int id;
  private String name;
  private double mass;
  private float radius;
  private PVector position;
  private PVector velocity;
  private int c;

  /**
  *  Default constructor for planets
  */
  
  public Projectile(PVector position, PVector heading, PVector velocity, int c) {
    // this.parent = parent;
    id = (int)random(1000, 4000);
    this.name = "";
    this.mass = DEF_MASS;
    this.radius = DEF_RADIUS;
    this.position = position;
    this.velocity = heading.setMag(DEF_SPEED).add(velocity);
    this.c = c;
  }
  
  // Draws the projectile
  public void draw() {
    stroke(this.c);
    fill(0);
    ellipseMode(RADIUS);
    ellipse(position.x, position.y, radius, radius);
  }
  
  public @Override
  PVector getInfluenceVector(ArrayList<SpaceObject> space) {
    PVector influence = new PVector(0, 0);
    for(int i = 0; i < space.size(); i++) {
      SpaceObject s = space.get(i);
      if(position.dist(s.getPosition()) < MAX_DISTANCE) {
        double r = PVector.dist(position, s.getPosition()) * SCALE;
        if(r == 0) return new PVector(0,0); // If the planet being checked is itself (or directly on top), don't move
        double force = G * ((mass * s.getMass()) / (r * r)); // G defined in orbit
        influence.add(new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag((float)(force / mass)));
      }
    }
    velocity.add(influence);
    return influence;
  }  
  
  public void update() {
    position.add(velocity);
  }  
  
  public int getID() { return id; }
  public String getName() { return name; }
  public int getColor() { return c; }
  public float getRadius() { return radius; }
  public PVector getVelocity() { return velocity; }
  public PVector getPosition() { return position; }
  public double getMass() { return mass; }
  
  public PVector addVelocity(PVector add) {
    return velocity.add(add);
  }
  
  public boolean collidesWith(SpaceObject s) {
    double r = PVector.dist(position, s.getPosition());
    if(r < (getRadius() + s.getRadius())) return true;
    return false;
  }  
}  


class Singleplayer implements Gamemode {
  
  int planetCount = 0;
  boolean dead = false;
  PVector pos;
  float spd;
  int health = 0;
  int ammunition = 100;
  String position;
  
  float shortestDistance = Integer.MAX_VALUE;
  
  Spaceship playerShip;
  
  int lastTrailDraw = -10; // For drawing trails
  float zoom = 1; // Camera zoom
  
  UniverseGen generator = new UniverseGen(8000, 120);
  
  ArrayList<Planet> planets = new ArrayList<Planet>();
  ArrayList<Spaceship> ships = new ArrayList<Spaceship>();
  ArrayList<ArrayList<PVector>> oldPositions;
  
  // UI Variables (not real time updated)
  float shortDist;
  float speed;
  
  public void init() {
    // Clear all previous data
    planets.clear();
    ships.clear();
    clearOverlay();
    
    background(0);
    
    frameCount = 0;
    dead = false;
    oldPositions = new ArrayList<ArrayList<PVector>>();
    generator = new UniverseGen(8000, 120);
    planets = generator.generate(); 
    
    ships.add(new Spaceship(
      "VEKTA I",
      5000,  // Mass
      5,     // Radius
      new PVector(1, 0), // Heading
      100, 100, // Position
      0, 0,    // Velocity
      color(0, 255, 0),
      0, 50, 60  // Control scheme, Speed, and Handling
    ));
    
    // Initialize trails
    for(SpaceObject p : planets) {
      ArrayList<PVector> init = new ArrayList<PVector>();
      oldPositions.add(init);
      oldPositions.get(p.getID()).add(new PVector(p.getPosition().x, p.getPosition().y));
    }
    
    ships.get(0).setID(planets.size());
    oldPositions.add(new ArrayList<PVector>());
    Spaceship s = ships.get(0);
    oldPositions.get(s.getID()).add(new PVector(s.getPosition().x, s.getPosition().y));
    
    playerShip = ships.get(0);
  }
  
  public void render() {
    background(0);
    SpaceObject markedForDeath = null;
    SpaceObject closestObject = null;
    shortestDistance = Integer.MAX_VALUE;
    
    if(!dead) {
      pos = ships.get(0).getPosition();
      spd = ships.get(0).getVelocity().mag();
      // Camera follow
      camera(pos.x, pos.y, (.07f*spd + .7f) * (height/2.0f) / tan(PI*30.0f / 180.0f) * zoom, pos.x, pos.y, 0.0f, 
       0.0f, 1.0f, 0.0f);
    }
    
    for(int i = 0; i < planets.size(); i++) {
      SpaceObject p = planets.get(i);
      if(planets.get(i).getPosition().dist(playerShip.getPosition()) < shortestDistance) {
        closestObject = planets.get(i);
        shortestDistance = (float)closestObject.getPosition().dist(playerShip.getPosition());
      }
      if(!paused) {
        ArrayList influencers = planets;
        p.getInfluenceVector((ArrayList<SpaceObject>)influencers);
      }
      for(SpaceObject p2 : planets) {
        if(p.collidesWith(p2) && p != p2) {
          // System.out.println(p + " has collided with " + p2);
          if(p.getMass() > p2.getMass()) {
            if(p.getRadius() < 3) {
              
            }  
            markedForDeath = p2; 
          }   
          else markedForDeath = p;
        };
      }
      for(Spaceship s : ships) {
        if(s.collidesWith(p)) {
          // System.out.println(s + " has collided with " + p);
          // Assumes ship explodes on contact with anything
          markedForDeath = s;
        }
        for(Projectile projectile : s.getProjectiles()) {
            if(projectile != null) {
              if(projectile.collidesWith(p)) {
                markedForDeath = p;
                s.removeProjectile(projectile);
              }
              for(Spaceship s2 : ships) {
                if(projectile.collidesWith(s2) && s != s2) {
                  markedForDeath = s2;
                }  
              }  
            }  
          }
        } 
        if(!paused) p.update();
        p.draw();
        drawTrail(p);
        
        planetCount++;
    }
    for(Spaceship s : ships) {
      if(!paused) {
          ArrayList influencers = planets;
          s.getInfluenceVector((ArrayList<SpaceObject>)influencers);
        }
      if(!paused) s.update();
      drawTrail(s);
      s.draw();
      for(Projectile projectile : s.getProjectiles()) {
        if(projectile != null) {
          if(!paused) {
            ArrayList influencers = planets;
            projectile.getInfluenceVector((ArrayList<SpaceObject>)influencers);
            projectile.update();
          }
           projectile.draw();
        }  
      }
    }
      
    if(markedForDeath != null) {
        // I DIED!
        if(markedForDeath instanceof Spaceship) {
          dead = true;
          if(settings[SETTINGS_SOUND] > 0) { 
            engine.stop();
            death.play();
          }
          // oldPositions.remove(ships.indexOf(markedForDeath) + objects.size());
          ships.remove(markedForDeath);
        }  
        // else oldPositions.remove(objects.indexOf(markedForDeath));
        planets.remove(markedForDeath);
      }  
    planetCount = 0;
    
    // Info
    if(!dead) {
      if(frameCount % 10 == 0) {  //  Update values every 10 frames
        Spaceship main = ships.get(0);
        health = 100;
        shortDist = shortestDistance;
        speed = (float)round(spd * 100) / 100;
        ammunition = main.getProjectilesLeft();
        position = round(pos.x) + ", " + round(pos.y);
      }  
      // GUI setup
      hint(DISABLE_DEPTH_TEST);
      camera();
      noLights();
      
      // Set text stuff
      textFont(bodyFont);
      textAlign(LEFT);
      textSize(16);
      // Draw a rectangle in the bottom
      fill(0);
      rectMode(CORNERS);
      rect(-1, height - 130, width + 1, height + 1);
      fill(0, 255, 0);
      // Text - Far right
      text("Health = " + health + "\nAmmunition = " + ammunition, width - 400, height - 100);
      // Text - left
      String closestObjectString;
      if(closestObject == null) {
        fill(100, 100, 100);
        closestObjectString = "Closest Object: None in range";
        shortestDistance = Integer.MAX_VALUE;
      } else {
        fill(closestObject.getColor());
        closestObjectString = "Closest Object: " + closestObject.getName() + " - " + shortDist + "AU";
      }  
      text(closestObjectString, 50, height - 100);
      if(closestObject != null) {
        stroke(closestObject.getColor());
        text("Mass: " + (float)round((float)((closestObject.getMass() / (1.989f * Math.pow(10, 30)) * 1000))) / 1000  + " Suns", 50, height - 80);
        // closest object arrow
        PVector arrow = closestObject.getPosition().sub(pos);
        arrow.normalize().mult(30);
        line(width/2, height - 65, width/2 + arrow.x, height - 65 + arrow.y);
        float angle = arrow.heading();
        float x = cos(angle);
        float y = sin(angle);
        PVector endpoint = new PVector(x, y);
        PVector arms = endpoint.copy();
        endpoint.mult(30);
        arms.mult(25); // scale the arms to a certain length
        // draw the arms
        line(width / 2 + endpoint.x, height - 65 + endpoint.y, width / 2 + cos(angle-.3f) * 25, height - 65 + sin(angle-.3f) * 25);
        line(width / 2 + endpoint.x, height - 65 + endpoint.y, width / 2 + cos(angle+.3f) * 25, height - 65 + sin(angle+.3f) * 25);
        
      }
      
    }
    
    // Menus
    else {
      hint(DISABLE_DEPTH_TEST);
      camera();
      noLights();
      textFont(headerFont);
      textAlign(CENTER, CENTER);
      
      // Header text
      stroke(0);
      fill(255, 0, 0);
      text("You died.", width / 2, (height / 2) - 100);
      
      // Body text
      stroke(0);
      fill(255);
      textFont(bodyFont);
      text("X TO RETRY", width / 2, (height / 2) + 97);
    }
    
    hint(ENABLE_DEPTH_TEST); 
    
  }  
  
  public void drawTrail(SpaceObject p) {
    ArrayList<PVector> old = oldPositions.get(p.getID());
    if(!paused) {
      if(old.size() > TRAIL_LENGTH) old.remove(0);
      old.add(new PVector(p.getPosition().x, p.getPosition().y));
    }  
    for(int i = 0; i < old.size() - 1; i++) {
      // Get two positions
      PVector oldPos = old.get(i);
      PVector newPos = old.get(i + 1);
      // Set the color and draw the line
      stroke(lerpColor(p.getColor(), color(0), (float)(old.size() - i) / old.size()));
      line(oldPos.x, oldPos.y,  newPos.x, newPos.y);
      // set oldPositions value
      oldPositions.set(p.getID(), old);
    }   
  }  
  
  public void keyPressed(char key) {
    if(dead) {
      if(key == 'x') {
        init();
      }
    } else {
     if(key == 'k') {
       dead = true;  
     }
     if(key == 'r' && zoom > 0.1f) {
       zoom -= 0.1f;
     }
     if(key == 'f') {
       zoom += 0.1f;
     }  
     for(Spaceship s : ships) {
       s.keyPress(key);
     }
    } 
  }
  
  public void keyReleased(char key) {
    for(Spaceship s : ships) {
      s.keyReleased(key);
    }
  }
}
interface SpaceObject {
  
  /** 
    Gets the unique ID of an object
  */
   public int getID();
  
  /**
   Gets the name of the object
 */
  public String getName();
  
  /**
    Gets the mass of the object
  */
  public double getMass();
  
  /**
    Gets the position of the object
  */
  public PVector getPosition();
  
  /**
    Gets the velocity of the object
  */
  public PVector getVelocity();
  
  /**
   Gets the color of the object
  */
  public int getColor();
  
  /**
    Gets the radius of the object (for collision purposes, not all objects are circular)
  */
  public float getRadius();
  
  /**
    Adds a vector to the velocity vector; returns new velocity
  */
  public PVector addVelocity(PVector add);
  
  /**
    Returns and applies the influence vector of another object on this
  */
  public PVector getInfluenceVector(ArrayList<SpaceObject> space);
  
  /**
    Does this collide with that?
  */
  public boolean collidesWith(SpaceObject s);
  
  public void draw();
  
  /**
    Update the position of this Object.
  */
  public void update();
}  
class Spaceship implements SpaceObject {
  // Default Shapeship settings
  private final double DEF_MASS = 1000;
  private final int DEF_RADIUS = 5;
  private final PVector DEF_POSITION = new PVector(100, 100);
  private final PVector DEF_VELOCITY = new PVector(0,0);
  private final PVector DEF_ACCELERATION = new PVector(0,0);
  private final PVector DEF_HEADING = new PVector(1,0,0);
  private final int DEF_COLOR = color(255, 255, 255);
  // Other default Spaceship stuff
  private final int DEF_SPEED = 100;
  private final int SPEED_SCALE = 500;
  private final int DEF_HANDLING = 50;
  private final int HANDLING_SCALE = 1000;
  private final int MAX_PROJECTILES = 100;

  // Exclusive Spaceship things
  private int controlScheme; // Defined by CONTROL_DEF: 0 = WASD, 1 = IJKL
  private float speed;  // Force of the vector added when engine is on
  private int handling; // Speed of turning
  private float turn; // Value used to turn the ship
  private Projectile[] projectiles = new Projectile[MAX_PROJECTILES];
  private int numProjectiles = 0;
  
  // Normal SpaceObject stuff
  private int id;
  private String name;
  private double mass;
  private float radius;
  private PVector position;
  private PVector velocity;
  private PVector heading;
  private PVector acceleration;
  private boolean accelerating;
  private boolean backwards;
  private int c;
  
  public Spaceship(String name, double mass, float radius, PVector heading, int x, int y, float xVelocity, float yVelocity, int c, int ctrl, int speed, int handling) {
    this.id = (int)random(4000, 10000);
    this.name = name;
    this.mass = mass;
    this.radius = radius;
    position = new PVector(x, y);
    velocity = new PVector(xVelocity, yVelocity);
    this.heading = heading;
    this.c = c;
    controlScheme = ctrl;
    this.speed = speed;
    this.handling = handling;
    acceleration = new PVector(0,0);
    
  }
  
  // Draws a nice triangle
  // Shamelessly stolen from https://processing.org/examples/flocking.html
  public void draw() {
    // Draw a triangle rotated in the direction of ship
    float theta = heading.heading() + radians(90);
    fill(1);
    stroke(c);
    pushMatrix();
    translate(position.x, position.y);
    rotate(theta);
    beginShape(TRIANGLES);
    vertex(0, -radius*2);
    vertex(-radius, radius*2);
    vertex(radius, radius*2);
    endShape();
    popMatrix();
  }
  
  // Update position
  public void update() {
    if(accelerating) {
      if(backwards) acceleration = heading.copy().setMag(-(float)speed / SPEED_SCALE);
      else          acceleration = heading.copy().setMag((float)speed / SPEED_SCALE);
    }
    velocity.add(acceleration);
    position.add(velocity);
    turnBy(turn);
  }  
  
  public void keyPress(char key) {
    heading.setMag(speed);
    if(controlScheme == 0) {   // WASD  
      switch(key) {
        case 'w':
          if(settings[SETTINGS_SOUND] > 0) {
            engine.stop();
            engine.loop();
          }
          accelerating = true;
          backwards = false;
          acceleration = heading.copy().setMag((float)speed / SPEED_SCALE);
          break;
        case 'a':
          turn = -((float)handling / HANDLING_SCALE);
          break;
        case 's':
          if(settings[SETTINGS_SOUND] > 0) {
            engine.stop();
            engine.loop();
          }
          accelerating = true;
          backwards = true;
          acceleration = heading.copy().setMag(-(float)speed / SPEED_SCALE);
          break;
        case 'd':
          turn = ((float)handling / HANDLING_SCALE);
          break;
        case 'x':
          fireProjectile();
          break;
        case 'z':
          fireProjectile();
          break;
      }  
    }
    if(controlScheme == 1) {   // IJKL  
      switch(key) {
        case 'i':
          if(settings[SETTINGS_SOUND] > 0) {
            engine.stop();
            engine.loop();
          }
          accelerating = true;
          backwards = false;
          acceleration = heading.copy().setMag((float)speed / SPEED_SCALE);
          break;
        case 'j':
          turn = -((float)handling / HANDLING_SCALE);
          break;
        case 'k':
          if(settings[SETTINGS_SOUND] > 0) {
            engine.stop();
            engine.loop();
          }
          accelerating = true;
          backwards = true;
          acceleration = heading.copy().setMag(-(float)speed / SPEED_SCALE);
          break;
        case 'l':
          turn = ((float)handling / HANDLING_SCALE);
          break;
        case 'm':
          fireProjectile();
          break;
        case ',':
          fireProjectile();
          break;
      }  
    } 
  }
  
  public void keyReleased(char key) {
    if((key == 'w' || key == 's') && controlScheme == 0) {
      if(settings[SETTINGS_SOUND] > 0) engine.stop();
      acceleration = DEF_ACCELERATION;
      accelerating = false;
      backwards = false;
    }
    if(( key == 'a' || key == 'd') && controlScheme == 0) {
      turn = 0;
    }
    
    if((key == 'i' || key == 'k') && controlScheme == 1) {
      if(settings[SETTINGS_SOUND] > 0) engine.stop();
      acceleration = DEF_ACCELERATION;
      accelerating = false;
      backwards = false;
    }
    if(( key == 'j' || key == 'l') && controlScheme == 1) {
      turn = 0;
    }  
  }  
  
  private void turnBy(float turnBy) {
    heading.rotate(turnBy);
  }  
  
  private void fireProjectile() {
    if(numProjectiles < MAX_PROJECTILES) {
      if(settings[SETTINGS_SOUND] > 0) laser.play();
      projectiles[numProjectiles] = new Projectile(position.copy(), heading.copy(), velocity.copy(), c);
      numProjectiles++;
    }
  }  
  
  /**
    Calculates influence of this Planet *from* another object in space.
  */
  public PVector getInfluenceVector(ArrayList<SpaceObject> space) {
    PVector influence = new PVector(0, 0);
    for(int i = 0; i < space.size(); i++) {
      SpaceObject s = space.get(i);
      float dist = position.dist(s.getPosition());
      if(dist < MAX_DISTANCE) {
        double r = dist * SCALE;
        if(r == 0) return new PVector(0,0); // If the planet being checked is itself (or directly on top), don't move
        double force = G * ((mass * s.getMass()) / (r * r)); // G defined in orbit
        influence.add(new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag((float)(force / mass)));
      }
    }
    stroke(255, 0, 0);
    line(position.x, position.y, position.x + (influence.x * 100), position.y + (influence.y * 100));
    velocity.add(influence);
    return influence;
  }  
  
  /**
    Checks if this planet collides with another planet
  */
  public boolean collidesWith(SpaceObject s) {
    double r = PVector.dist(position, s.getPosition());
    if(r < (getRadius() + s.getRadius())) return true;
    return false;
  }  
  
  // GETTERS / SETTERS ---------------------------------------------------------------
  
  public int getID() { return id; }
  
  public void setID(int id) { this.id = id; }
  
  public String getName() {
    return name;
  }  
  
  public Projectile[] getProjectiles() {
    return projectiles;
  }
  
  public void setProjectiles(Projectile[] p) {
    projectiles = p;
  }
  
  public int getProjectilesLeft() {
    return MAX_PROJECTILES - numProjectiles;
  }
  
  public void removeProjectile(Projectile p) {
    boolean found = false;
    int i = 0;
    while(!found) {
      if(projectiles[i] == p) {
        found = true;
        projectiles[i] = null;
        for(int j = projectiles.length - 1; j > i; j--) {
          projectiles[j] = projectiles[j - 1];
        }
      }
      i++;
    }  
  }  
  
  public int decrementProjectiles() {
    return numProjectiles--;
  }  
  
  public @Override
  double getMass() {
    return mass;
  }
  
  // Mass setter
  public void setMass(int mass) {
    this.mass = mass;
  }
  
  public @Override
  float getRadius() {
    return radius;
  }
  
  // Radius setter
  public void setRadius(int radius) {
    this.radius = radius;
  }
  
  public int getColor() {
    return c;
  }  
  
  public void setColor(int c) {
    this.c = c;
  }  
  
  public @Override
  PVector getPosition() {
    return position;
  }
  
  public @Override
  PVector getVelocity() {
    return velocity;
  }
  
  public @Override
  PVector addVelocity(PVector add) {
    return velocity.add(add);
  }
}  
class UniverseGen {
  int size;
  int density;
  ArrayList<Planet> space = new ArrayList<Planet>();
  
  int currentID;
  
  public UniverseGen(int size, int density) {
    this.size = size;
    this.density = density;
    currentID = 0;
  }
  
  public ArrayList<Planet> generate() {
    for(int i = 0; i < density; i++) {
      float[] coords = {0, 0};
      boolean unique = false;
      
      while(!unique) {
        coords = generateCoordinates(size);
        unique = true;
        for(Planet s : space) {
          if(s.getPosition().x == coords[0] && s.getPosition().y == coords[1]) {
            unique = false;
          }
        }
      }
      ArrayList<Planet> system = createSystem((int)coords[0], (int)coords[1]);
      for(int j = 0; j < system.size(); j++) {
        space.add(system.get(j));
      }
    }
    return space;
  }
  
  private ArrayList<Planet> createSystem(int x, int y) {
    ArrayList<Planet> system = new ArrayList<Planet>();
    // Create the center body
    double power = Math.pow(10, random(28, 31));
    double centerMass = random(0.8f, 4) * power;
    float radius = (float)(random(3, 6) * (centerMass / power));
    system.add(new Planet(
      currentID,
      centerMass, // Mass
      radius,   // Radius
      x, y,  // Coords
      0, 0,  // Velocity
      color(random(100, 255), random(100, 255), random(100, 255))
    ));
    currentID++;
    
    // Generate planets around body
    int planets = (int)random(1, 8);
    for(int i = 0; i <= planets; i++) {
      float radiusLoc = (float)(random(600, 2000) * SCALE);
      float velocity = sqrt((float)(G * centerMass / radiusLoc));
      double mass = random(0.8f, 4) * power;
      float radiusSize = (float)(random(2, 5) * (mass / power));
      System.out.println(radiusSize);
      system.add(new Planet(
        currentID,
        mass, // Mass
        radiusSize,   // Radius
        (int)(x + radiusLoc), y,  // Coords
        0, (float)(velocity),  // Velocity
        color(random(100, 255), random(100, 255), random(100, 255))
      ));
      currentID++;
    }  
    return system;
  }
  
  private float[] generateCoordinates(float max) {
    float[] retVal = {random(max), random(max)};
    return retVal;
  }  
}  
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "vekta" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
