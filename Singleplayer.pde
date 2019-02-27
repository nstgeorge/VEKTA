import java.util.*;

private static int nextID = 0;

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
  
  List<SpaceObject> objects = new ArrayList<SpaceObject>();
  
  List<SpaceObject> markedForDeath = new ArrayList<SpaceObject>();
  List<SpaceObject> markedForAddition = new ArrayList<SpaceObject>();
  
  // UI Variables (not real time updated)
  float shortDist;
  float speed;
  
  @Override
  void init() {
    background(0);
    
    if(getSetting("music") > 0 && !atmosphere.isPlaying()) atmosphere.loop();
    
    frameCount = 0;
    dead = false;
    generator = new UniverseGen(8000, 120);
    
    // Add initial planets
    for(Planet p : generator.generate()) {
      addObject(p);
    }
    
    playerShip = new Spaceship(
      "VEKTA I",
      5000,  // Mass
      5,     // Radius
      new PVector(1, 0), // Heading
      new PVector(), // Position
      new PVector(),    // Velocity
      color(0, 255, 0),
      0, 50, 60  // Control scheme, Speed, and Handling
    );
    addObject(playerShip);
  }
  
  @Override
  void render() {
    background(0);
    
    if(!dead) {
      pos = playerShip.getPosition();
      spd = playerShip.getVelocity().mag();
      // Camera follow
      camera(pos.x, pos.y, (.07*spd + .7) * (height/2.0) / tan(PI*30.0 / 180.0) * zoom, pos.x, pos.y, 0.0, 
       0.0, 1.0, 0.0);
    }
    
    if(paused) {
      return;
    }
    
    SpaceObject closestObject = null;
    shortestDistance = Integer.MAX_VALUE;
    for(SpaceObject s : objects) {
      if(s instanceof Planet) {
        planetCount++;
        if(s.getPosition().dist(playerShip.getPosition()) < shortestDistance) {
          closestObject = s;
          shortestDistance = s.getPosition().dist(playerShip.getPosition());
        }
      }
      
      s.applyInfluenceVector(objects);
      
      for(SpaceObject other : objects) {
        if(s != other && s.collidesWith(other)/* && !markedForDeath.contains(other)*/) {
          if(other.shouldDestroy(s)) {
            s.onDestroy(other);
            removeObject(s);
          }
        }
      }
      s.update();
      s.draw();
      s.drawTrail();
    }
    
    objects.removeAll(markedForDeath);
    objects.addAll(markedForAddition);
    markedForDeath.clear();
    markedForAddition.clear();
    
    /*for(Spaceship s : ships) {
      
      drawTrail(s);
      s.draw();
      for(Projectile projectile : s.getProjectiles()) {
        if(projectile != null) {
          if(!paused) {
            List influencers = planets;
            projectile.applyInfluenceVector((List<SpaceObject>)influencers);
            projectile.update();
          }
           projectile.draw();
        }  
      }
    }*/
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
      drawDial("Heading", playerShip.getHeading(), width - 370, height - 65, 50, color(0, 255, 0));
      drawDial("Velocity", playerShip.getVelocity().copy(), width - 500, height - 65, 50, color(0, 255, 0));
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
  
  void updateUIInformation() {
    health = 100;
    shortDist = (float)round(shortestDistance * 100) / 100;
    speed = (float)round(spd * 100) / 100;
    ammunition = playerShip.getProjectilesLeft();
    position = round(pos.x) + ", " + round(pos.y);
  }
  
  @Override
  void keyPressed(char key) {
    if(dead) {
      if(key == 'x') {
        lowPass.stop();
        restart();
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
     playerShip.keyPress(key);
    } 
  }
  
  @Override
  void keyReleased(char key) {
    playerShip.keyReleased(key);
  }
  
  @Override
  void mouseWheel(float amount) {
    zoom = max(.1, min(3, zoom * (1 + amount * .1)));
  }
  
  @Override
  void restart() {
    startGamemode(new Singleplayer());
  }
  
  @Override
  boolean addObject(Object object) {
    if(object instanceof SpaceObject) {
      SpaceObject s = (SpaceObject)object;
      s.setID(nextID++);
      markedForAddition.add(s);
      return true;
    }
    return false;
  }
  
  @Override
  boolean removeObject(Object object) {
    if(object instanceof SpaceObject) {
      markedForDeath.add((SpaceObject)object);
      return true;
    }
    return false;
  }
}
