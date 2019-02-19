class Spaceship implements SpaceObject {
  // Default Shapeship settings
  private final double DEF_MASS = 1000;
  private final int DEF_RADIUS = 5;
  private final PVector DEF_POSITION = new PVector(100, 100);
  private final PVector DEF_VELOCITY = new PVector(0,0);
  private final PVector DEF_ACCELERATION = new PVector(0,0);
  private final PVector DEF_HEADING = new PVector(1,0,0);
  private final color DEF_COLOR = color(255, 255, 255);
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
  private color c;
  
  public Spaceship(String name, double mass, float radius, PVector heading, int x, int y, float xVelocity, float yVelocity, color c, int ctrl, int speed, int handling) {
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
  void draw() {
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
  void update() {
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
  
  void keyReleased(char key) {
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
    Calculates influence of this ship *from* another object in space.
  */
  PVector getInfluenceVector(ArrayList<SpaceObject> space) {
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
    
    velocity.add(influence);
    return influence;
  }  
  
  /**
    Checks if this planet collides with another planet
  */
  boolean collidesWith(SpaceObject s) {
    double r = PVector.dist(position, s.getPosition());
    if(r < (getRadius() + s.getRadius())) return true;
    return false;
  }  
  
  // GETTERS / SETTERS ---------------------------------------------------------------
  
  int getID() { return id; }
  
  void setID(int id) { this.id = id; }
  
  String getName() {
    return name;
  }  
  
  Projectile[] getProjectiles() {
    return projectiles;
  }
  
  void setProjectiles(Projectile[] p) {
    projectiles = p;
  }
  
  int getProjectilesLeft() {
    return MAX_PROJECTILES - numProjectiles;
  }
  
  void removeProjectile(Projectile p) {
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
  
  int decrementProjectiles() {
    return numProjectiles--;
  }  
  
  @Override
  double getMass() {
    return mass;
  }
  
  // Mass setter
  void setMass(int mass) {
    this.mass = mass;
  }
  
  @Override
  float getRadius() {
    return radius;
  }
  
  // Radius setter
  void setRadius(int radius) {
    this.radius = radius;
  }
  
  color getColor() {
    return c;
  }  
  
  void setColor(color c) {
    this.c = c;
  }  
  
  @Override
  PVector getPosition() {
    return position;
  }
  
  @Override
  PVector getVelocity() {
    return velocity;
  }
  
  @Override
  PVector addVelocity(PVector add) {
    return velocity.add(add);
  }
}  
