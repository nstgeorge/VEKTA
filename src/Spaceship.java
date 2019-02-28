import processing.core.*;

import java.util.List;

import static processing.core.PConstants.TRIANGLES;

class Spaceship extends SpaceObject {
  // Default Shapeship settings
  private final double DEF_MASS = 1000;
  private final int DEF_RADIUS = 5;
  private final PVector DEF_POSITION = new PVector(100, 100);
  private final PVector DEF_VELOCITY = new PVector(0,0);
  private final PVector DEF_ACCELERATION = new PVector(0,0);
  private final PVector DEF_HEADING = new PVector(1,0,0);
  private final Color DEF_COLOR = new Color(255, 255, 255);
  // Other default Spaceship stuff
  private final int DEF_SPEED = 100;
  private final int DEF_HANDLING = 50;
  private final int HANDLING_SCALE = 1000;
  private final int MAX_PROJECTILES = 100;
  private final float LANDING_SPEED = .5F;
  
  // Backreference to world (exclusive to spaceships for now)
  // At some point, the `Singleplayer` class will be split into world and control containers, or something similar
  private final Singleplayer world;
  
  // Exclusive Spaceship things
  private int controlScheme; // Defined by CONTROL_DEF: 0 = WASD, 1 = IJKL
  private float speed;  // Force of the vector added when engine is on
  private int handling; // Speed of turning
  private int numProjectiles = 0;
  private float thrust;
  private float turn;
  
  // Normal SpaceObject stuff
  private final String name;
  private final float mass;
  private final float radius;
  private final PVector heading;
  private final Color c;
  
  // Landing doodads
  private boolean landing;
  private final PVector influence = new PVector();
  
  private final Inventory inventory = new Inventory();
  
  public Spaceship(Singleplayer world, PApplet parent, String name, float mass, float radius, PVector heading, PVector position, PVector velocity, Color c, int ctrl, float speed, int handling, int money) {
    super(parent, position, velocity);
    this.world = world;
    this.name = name;
    this.mass = mass;
    this.radius = radius;
    this.heading = heading;
    this.c = c;
    this.controlScheme = ctrl;
    this.speed = speed;
    this.handling = handling;
    this.inventory.add(money);
  }
  
  // Draws a nice triangle
  // Shamelessly stolen from https://processing.org/examples/flocking.html
  @Override
  void draw() {
    // Draw a triangle rotated in the direction of ship
    float theta = heading.heading() + processing.core.PApplet.radians(90);
    super.parent.fill(1);
    super.parent.stroke(c.getIntValue());
    super.parent.pushMatrix();
    super.parent.translate(position.x, position.y);
    super.parent.rotate(theta);
    super.parent.beginShape(TRIANGLES);
    super.parent.vertex(0, -radius*2);
    super.parent.vertex(-radius, radius*2);
    super.parent.vertex(radius, radius*2);
    super.parent.endShape();
    super.parent.popMatrix();
    
    // Draw influence vector
    super.parent.stroke(255, 0, 0);
    super.parent.line(position.x, position.y, position.x + (influence.x * 100), position.y + (influence.y * 100));
  }
  
  @Override
  void onUpdate() {
    if(thrust != 0) {
      addVelocity(heading.copy().setMag(thrust * speed));
    }
    turnBy(turn);
    
    if(landing && world.closestObject != null) {
      SpaceObject target = world.closestObject;
      PVector relative = velocity.copy().sub(target.getVelocity());
      float mag = relative.mag();
      if(mag > 0) {
        heading.set(relative).normalize();
        float approachFactor = Math.min(1, 5 * target.getRadius() / target.getPosition().sub(position).mag());
        thrust = Math.max(-1, Math.min(1, (LANDING_SPEED * (1 - approachFactor / 2) - mag) * approachFactor / speed));
      }
    }
  }
  
  public void keyPress(char key) {
    landing = false;
    if(controlScheme == 0) {   // WASD
      switch(key) {
        case 'w':
          if(Vekta.getInstance().getSetting("sound") > 0) {
            engine.stop();
            engine.loop();
          }
          thrust = 1;
          break;
        case 'a':
          turn = -((float)handling / HANDLING_SCALE);
          break;
        case 's':
          if(Vekta.getInstance().getSetting("sound") > 0) {
            engine.stop();
            engine.loop();
          }
          thrust = -1;
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
        case '\t':
          landing = true;
          break;
      }
    }
    // TODO: map these keys using a config object rather than as switch statements
    if(controlScheme == 1) {   // IJKL
      switch(key) {
        case 'i':
          if(Vekta.getInstance().getSetting("sound") > 0) {
            engine.stop();
            engine.loop();
          }
          thrust = 1;
          break;
        case 'j':
          turn = -((float)handling / HANDLING_SCALE);
          break;
        case 'k':
          if(Vekta.getInstance().getSetting("sound") > 0) {
            engine.stop();
            engine.loop();
          }
          thrust = -1;
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
        case '\\':
          landing = true;
          break;
      }
    } 
  }
  
  void keyReleased(char key) {
    if((key == 'w' || key == 's') && controlScheme == 0) {
      if(Vekta.getInstance().getSetting("sound") > 0) engine.stop();
      thrust = 0;
    }
    if(( key == 'a' || key == 'd') && controlScheme == 0) {
      turn = 0;
    }
    
    if((key == 'i' || key == 'k') && controlScheme == 1) {
      if(Vekta.getInstance().getSetting("sound") > 0) engine.stop();
      thrust = 0;
    }
    if((key == 'j' || key == 'l') && controlScheme == 1) {
      turn = 0;
    }
  }  
  
  private void turnBy(float turnBy) {
    heading.rotate(turnBy);
  }  
  
  private void fireProjectile() {
    if(numProjectiles < MAX_PROJECTILES) {
      if(Vekta.getInstance().getSetting("sound") > 0) laser.play();
      Vekta.getInstance().addObject(new Projectile(this, position.copy(), velocity.copy(), heading.copy(), c));
      numProjectiles++;
    }
  }
  
  @Override
  void onDestroy(SpaceObject s) {
    lowPass.process(atmosphere, 800);
    
    getWorld().dead = true;
    if(Vekta.getInstance().getSetting("sound") > 0) {
      engine.stop();
      death.play();
    }
  }
  
  Singleplayer getWorld() {
    return world;
  }
  
  Inventory getInventory() {
    return inventory;
  }
  
  boolean isLanding() {
    return landing;
  }
  
  @Override
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
  float getMass() {
    return mass;
  }
  
  @Override
  float getRadius() {
    return radius;
  }
  
  @Override
  Color getColor() {
    return c;
  }
  
  PVector getHeading() {
    return heading;
  }
  
  @Override
  PVector applyInfluenceVector(List<SpaceObject> objects) {
    this.influence.set(super.applyInfluenceVector(objects));
    return this.influence;
  }
  
  @Override
  boolean collidesWith(SpaceObject s) {
    // TODO: generalize, perhaps by adding SpaceObject::getParent(..) and handling this case in SpaceObject
    return !(s instanceof Projectile && ((Projectile)s).getParent() == this) && super.collidesWith(s);
  }
  
  void onTakeoff() {
    
  }
}  
