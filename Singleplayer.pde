import java.util.*;

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
  
  UniverseGen generator = new UniverseGen(80000, 1200);
  
  ArrayList<Planet> planets = new ArrayList<Planet>();
  ArrayList<Spaceship> ships = new ArrayList<Spaceship>();
  ArrayList<ArrayList<PVector>> oldPositions;
  
  ArrayList<SpaceObject> markedForDeath = new ArrayList<SpaceObject>();
  
  // UI Variables (not real time updated)
  float shortDist;
  float speed;
  
  void init() {
    // Clear all previous data
    planets.clear();
    ships.clear();
    clearOverlay();
    
    background(0);
    
    if(getSetting("music") > 0 && !atmosphere.isPlaying()) atmosphere.loop();
    
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
    
    playerShip = ships.get(0);
    playerShip.setID(planets.size());
    oldPositions.add(new ArrayList<PVector>());
    Spaceship s = playerShip;
    oldPositions.get(s.getID()).add(new PVector(s.getPosition().x, s.getPosition().y));
  }
  
  void render() {
    background(0);
    SpaceObject closestObject = null;
    shortestDistance = Integer.MAX_VALUE;
    
    if(!dead) {
      pos = playerShip.getPosition();
      spd = playerShip.getVelocity().mag();
      // Camera follow
      camera(pos.x, pos.y, (.07*spd + .7) * (height/2.0) / tan(PI*30.0 / 180.0) * zoom, pos.x, pos.y, 0.0, 
       0.0, 1.0, 0.0);
    }
    
    for(SpaceObject p : planets) {
      if(p.getPosition().dist(playerShip.getPosition()) < shortestDistance) {
        closestObject = p;
        shortestDistance = (float)p.getPosition().dist(playerShip.getPosition());
      }
      if(!paused) {
        ArrayList influencers = planets;
        p.getInfluenceVector((ArrayList<SpaceObject>)influencers);
      }
      for(SpaceObject p2 : planets) {
        if(p.collidesWith(p2) && p != p2) {
          if(p.getMass() > p2.getMass()) {
            // TODO: refactor onDestroy(..) and markedForDeath.add(..) logic to a separate method
            p2.onDestroy(p);
            markedForDeath.add(p2);
          }
          else {
            p.onDestroy(p2);
            markedForDeath.add(p);
          }
        }
      }
      for(Spaceship s : ships) {
        if(s.collidesWith(p)) {
          // Assumes ship explodes on contact with anything
          s.onDestroy(p);
          dead = true;
          if(getSetting("sound") > 0) { 
            engine.stop();
            death.play();
          }
          markedForDeath.add(s);
        }
        for(Projectile projectile : s.getProjectiles()) {
            if(projectile != null) {
              if(projectile.collidesWith(p)) {
                p.onDestroy(projectile);
                markedForDeath.add(p);
                s.removeProjectile(projectile);
              }
              for(Spaceship s2 : ships) {
                if(projectile.collidesWith(s2) && s != s2) {
                  s2.onDestroy(projectile);
                  markedForDeath.add(s2);
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
    for(SpaceObject s : markedForDeath) {
      if(s instanceof Spaceship) {
        ships.remove(s);
      }
      if(s instanceof Planet) {
        planets.remove(s);
      }  
    }
    for(Spaceship s : ships) {
      if(!paused) {
        ArrayList influencers = planets;
        PVector influence = s.getInfluenceVector((ArrayList<SpaceObject>)influencers);
        s.update();
        // Draw influence vector on ship
        stroke(255, 0, 0);
        line(s.getPosition().x, s.getPosition().y, s.getPosition().x + (influence.x * 100), s.getPosition().y + (influence.y * 100));
      }
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
    planetCount = 0;
    
    // Info
    if(!dead) {
      if(frameCount % 10 == 0) {  //  Update values every 10 frames
        updateUIInformation();
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
      text("Health = " + health + "\nAmmunition = " + ammunition, width - 300, height - 100);
      // Ship heading indicator
      drawDial("Heading", ships.get(0).getHeading(), width - 370, height - 65, 50, color(0, 255, 0));
      drawDial("Velocity", ships.get(0).getVelocity().copy(), width - 500, height - 65, 50, color(0, 255, 0));
      // Text - left
      String closestObjectString;
      if(closestObject == null) {
        fill(100, 100, 100);
        closestObjectString = "Closest Object: None in range";
        shortestDistance = Integer.MAX_VALUE;
      } else {
        closestObjectString = "Closest Object: " + closestObject.getName() + " - " + shortDist + "AU \nSpeed: "+ (float)round(closestObject.getVelocity().mag() * 100) / 100 + "\nMass: " + (float)round((float)((closestObject.getMass() / (1.989 * Math.pow(10, 30)) * 1000))) / 1000  + " Suns";
        // Closest object arrow
        drawDial("Direction", closestObject.getPosition().sub(pos), 450, height - 65, 50, closestObject.getColor());
        stroke(closestObject.getColor());
        fill(closestObject.getColor());
      }  
      text(closestObjectString, 50, height - 100);
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
  
  void drawDial(String name, PVector info, int locX, int locY, int radius, color c) {
      fill(0);
      stroke(c);
      ellipse(locX, locY, radius, radius);
      fill(100, 100, 100);
      textAlign(CENTER);
      textSize(14);
      text(name, locX, locY + 25);
      textAlign(LEFT);
      textSize(16);
      stroke(c);
      drawArrow(info, (int)(radius * .8), locX, locY);
      // Reset colors
      fill(0);
      stroke(0, 255, 0);
  }
  
  void drawArrow(PVector heading, int length, int locX, int locY) {
    heading.normalize().mult(length);
    line(locX,  locY, locX + heading.x, locY + heading.y);
    float angle = heading.heading();
    float x = cos(angle);
    float y = sin(angle);
    PVector endpoint = new PVector(x, y);
    PVector arms = endpoint.copy();
    endpoint.mult(length);
    arms.mult(length *.8); // scale the arms to a certain length
    // draw the arms
    line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle-.3) * (length *.8), locY + sin(angle-.3) * (length *.8));
    line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle+.3) * (length *.8), locY + sin(angle+.3) * (length *.8));
  }
  
  void drawTrail(SpaceObject p) {
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
  
  void updateUIInformation() {
    Spaceship main = ships.get(0);
    health = 100;
    shortDist = (float)round(shortestDistance * 100) / 100;
    speed = (float)round(spd * 100) / 100;
    ammunition = main.getProjectilesLeft();
    position = round(pos.x) + ", " + round(pos.y);
  }
  
  void keyPressed(char key) {
    if(dead) {
      if(key == 'x') {
        lowPass.stop();
        init();
      }
    } else {
     if(key == 'k') {
       dead = true;  
     }
     if(key == 'r') {
       mouseWheel(-1);
     }
     if(key == 'f') {
       mouseWheel(1);
     }
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
  
  void mouseWheel(float amount) {
    zoom = max(.1, min(3, zoom * (1 + amount * .1)));
  }
}
