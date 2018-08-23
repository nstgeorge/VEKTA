import java.util.*;

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
