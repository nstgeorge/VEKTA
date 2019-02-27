class Spaceship extends SpaceObject {
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
  private int numProjectiles = 0;
  
  // Normal SpaceObject stuff
  private String name;
  private double mass;
  private float radius;
  private PVector heading;
  private PVector acceleration;
  private boolean accelerating;
  private boolean backwards;
  private color c;
  
  public Spaceship(String name, double mass, float radius, PVector heading, PVector position, PVector velocity, color c, int ctrl, int speed, int handling) {
    super(position, velocity);
    this.name = name;
    this.mass = mass;
    this.radius = radius;
    this.heading = heading;
    this.c = c;
    controlScheme = ctrl;
    this.speed = speed;
    this.handling = handling;
    acceleration = new PVector();
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
          if(getSetting("sound") > 0) {
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
          if(getSetting("sound") > 0) {
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
          if(getSetting("sound") > 0) {
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
          if(getSetting("sound") > 0) {
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
      if(getSetting("sound") > 0) engine.stop();
      acceleration = DEF_ACCELERATION;
      accelerating = false;
      backwards = false;
    }
    if(( key == 'a' || key == 'd') && controlScheme == 0) {
      turn = 0;
    }
    
    if((key == 'i' || key == 'k') && controlScheme == 1) {
      if(getSetting("sound") > 0) engine.stop();
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
      if(getSetting("sound") > 0) laser.play();
      addObject(new Projectile(this, position.copy(), heading.copy(), velocity.copy(), c));
      numProjectiles++;
    }
  }
  
  void onDestroy(SpaceObject s) {
    lowPass.process(atmosphere, 800);
    
    // TODO: avoid cast
    ((Singleplayer)game).dead = true;
    if(getSetting("sound") > 0) { 
      engine.stop();
      death.play();
    }
  }
  
  // GETTERS / SETTERS ---------------------------------------------------------------
  
  String getName() {
    return name;
  }
  
  int getProjectilesLeft() {
    return MAX_PROJECTILES - numProjectiles;
  }
  
  int decrementProjectiles() {
    return numProjectiles--;
  }
  
  @Override
  double getMass() {
    return mass;
  }
  
  @Override
  float getRadius() {
    return radius;
  }
  
  @Override
  color getColor() {
    return c;
  }  
  
  void setColor(color c) {
    this.c = c;
  }
  
  PVector getHeading() {
    return heading;
  }
  
  @Override
  PVector applyInfluenceVector(List<SpaceObject> objects) {
    PVector influence = super.applyInfluenceVector(objects);
    
    // Draw influence vector
    stroke(255, 0, 0);
    line(position.x, position.y, position.x + (influence.x * 100), position.y + (influence.y * 100));
    
    return influence;
  }
  
  @Override
  boolean collidesWith(SpaceObject s) {
    // TODO: generalize, perhaps by adding SpaceObject::getParent(..) and handling this case in SpaceObject
    return !(s instanceof Projectile && ((Projectile)s).getParent() == this) && super.collidesWith(s);
  }
}  
