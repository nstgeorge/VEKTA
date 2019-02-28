/**
  A landing site for one spacecraft-like object.
  State management for landing sites should be handled or proxied through this class.
*/
class LandingSite {
  // TODO: convert this to an interface or abstract class if necessary
  
  public SpaceObject landed;
  
  public LandingSite() {
    
  }
  
  public boolean land(Spaceship ship) {
    if(landed != null) {
      return false;
    }
    
    landed = ship;
    removeObject(ship);
    return true;
  }
  
  public boolean launch() {
    if(landed == null) {
      return false;
    }
    
    //TODO implement
    return true;
  }
}
