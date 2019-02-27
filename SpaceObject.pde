private static final float MAX_INFLUENCE_ACCEL = 10;

abstract class SpaceObject {
  private int id;
  
  /** 
    Gets the unique ID of an object
  */
   final int getID() {
     return id;
   }
   
  /** 
    Sets the unique ID of an object
  */
   final void setID(int id) {
     this.id = id;
   }
  
  /**
   Gets the name of the object
 */
  abstract String getName();
  
  /**
    Sets the mass of the object
  */
  abstract void setMass(double mass);
  
  /**
    Gets the mass of the object
  */
  abstract double getMass();
  
  /**
    Gets the position of the object
  */
  abstract PVector getPosition();
  
  /**
    Gets the velocity of the object
  */
  abstract PVector getVelocity();
  
  /**
    Gets the velocity of the object
  */
  abstract PVector addVelocity(PVector add);
  
  /**
   Gets the color of the object
  */
  abstract color getColor();
  
  /**
    Set the radius of this object
  */
  abstract void setRadius(float radius);
  
  /**
    Gets the radius of the object (for collision purposes, not all objects are circular)
  */
  abstract float getRadius();
  
  /**
    Returns and applies the influence vector of another object on this
  */
  PVector applyInfluenceVector(List<SpaceObject> space) {
    double mass = getMass();
    PVector velocity = getVelocity();
    PVector position = getPosition();
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
      // Prevent insane acceleration
      influence.limit(MAX_INFLUENCE_ACCEL);
      velocity.add(influence);
    }
    // TODO: is this supposed to be `return velocity;`?
    return new PVector();
  }
  
  /**
    Does this collide with that?
  */
  boolean collidesWith(SpaceObject s) {
    double r = PVector.dist(getPosition(), s.getPosition());
    if(r < (getRadius() + s.getRadius())) return true;
    return false;
  }
  
  /**
    Do this when destroyed by SpaceObject s
  */
  void onDestroy(SpaceObject s) {}
  
  void draw() {}
  
  /**
    Update the position of this Object.
  */
  void update() {}
}  
