interface SpaceObject {
  
  /** 
    Gets the unique ID of an object
  */
   int getID();
   
  /** 
    Sets the unique ID of an object
  */
   void setID(int id);
  
  /**
   Gets the name of the object
 */
  String getName();
  
  /**
    Sets the mass of the object
  */
  void setMass(double mass);
  
  /**
    Gets the mass of the object
  */
  double getMass();
  
  /**
    Gets the position of the object
  */
  PVector getPosition();
  
  /**
    Gets the velocity of the object
  */
  PVector getVelocity();
  
  /**
   Gets the color of the object
  */
  color getColor();
  
  /**
    Set the radius of this object
  */
  void setRadius(float radius);
  
  /**
    Gets the radius of the object (for collision purposes, not all objects are circular)
  */
  float getRadius();
  
  /**
    Adds a vector to the velocity vector; returns new velocity
  */
  PVector addVelocity(PVector add);
  
  /**
    Returns and applies the influence vector of another object on this
  */
  PVector applyInfluenceVector(ArrayList<SpaceObject> space);
  
  /**
    Does this collide with that?
  */
  boolean collidesWith(SpaceObject s);
  
  /**
    Do this when destroyed by SpaceObject s
  */
  void onDestroy(SpaceObject s);
  
  void draw();
  
  /**
    Update the position of this Object.
  */
  void update();
}  
