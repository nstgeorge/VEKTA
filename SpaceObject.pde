private static final float MAX_INFLUENCE = 2;

abstract class SpaceObject {
  private int id;
  
  PVector position;
  PVector velocity;
  
  PVector[] trail = new PVector[TRAIL_LENGTH];
  
  SpaceObject() {
    this(new PVector(), new PVector());
  }
  
  SpaceObject(PVector position, PVector velocity) {
    this.position = position;
    this.velocity = velocity;
  }
  
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
  //abstract void setMass(double mass);
  
  /**
    Gets the mass of the object
  */
  abstract float getMass();
  
  /**
    Gets the position of the object
  */
  final PVector getPosition() {
    return position.copy(); // TODO: copy externally for performance
  }
  
  /**
    Gets the velocity of the object
  */
  final PVector getVelocity() {
    return velocity.copy(); // TODO: copy externally for performance
  }
  
  /**
    Gets the velocity of the object
  */
  final PVector addVelocity(PVector add) {
    return velocity.add(add);
  }
  
  /**
   Gets the color of the object
  */
  abstract color getColor();
  
  /**
    Set the radius of this object
  */
  //abstract void setRadius(float radius);
  
  /**
    Gets the radius of the object (for collision purposes, not all objects are circular)
  */
  abstract float getRadius();
  
  /**
    Returns and applies the influence vector of another object on this
  */
  PVector applyInfluenceVector(List<SpaceObject> objects) {
    float mass = getMass();
    PVector influence = new PVector();
    for(SpaceObject s : objects) {
      float distSq = getDistSq(position, s.getPosition());
      if(distSq == 0) continue; // If the planet being checked is itself (or directly on top), don't move
      float force = G * mass * s.getMass() / (distSq * SCALE * SCALE); // G defined in orbit
      influence.add(new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag(force / mass));
    }
    // Prevent insane acceleration
    influence.limit(MAX_INFLUENCE);
    if(!Float.isFinite(influence.x) || !Float.isFinite(influence.y)) {
      // This helps prevent the random blank screen of doom (NaN propagation)
      return new PVector();
    }
    addVelocity(influence);
    return influence;
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
    When colliding, does this object destroy the other?
    By default, the other object will be destroyed if this mass is at least half of their mass. 
  */
  boolean shouldDestroy(SpaceObject other) {
    return getMass() * 2 >= other.getMass();
  }
  
  /**
    Do this when destroyed by SpaceObject `s`
  */
  void onDestroy(SpaceObject s) {}
  
  abstract void draw();
  
  void drawTrail() {
    // Update trail vectors
    for(int i = trail.length - 1; i > 0; i--) {
      trail[i] = trail[i - 1];
    }
    trail[0] = position.copy();
    
    for(int i = 1; i < trail.length; i++) {
      PVector oldPos = trail[i - 1];
      PVector newPos = trail[i];
      if(newPos == null) {
        break;
      }
      // Set the color and draw the line segment
      stroke(lerpColor(getColor(), color(0), (float)i / trail.length));
      line(oldPos.x, oldPos.y,  newPos.x, newPos.y);
    }
  }
  
  /**
    Update the position of this SpaceObject.
  */
  void update() {}
}  
