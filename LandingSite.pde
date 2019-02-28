/**
  A landing site for one spacecraft-like object.
  State management for landing sites should be handled or proxied through this class.
*/
class LandingSite {
  // TODO: convert this to an interface or abstract class if necessary
  
  private final SpaceObject launchObject;
  
  private Spaceship landed;
  
  private LandingMenu menu;
  
  public LandingSite(SpaceObject launchObject) {
    this.launchObject = launchObject;
  }
  
  public boolean land(Spaceship ship) {
    if(landed != null) {
      return false;
    }
    
    landed = ship;
    removeObject(ship);
    menu = new LandingMenu(this);
    openMenu(menu); // Push landing menu to global menu stack
    return true;
  }
  
  public boolean takeoff() {
    if(landed == null) {
      return false;
    }
    
    closeMenu(menu);
    menu = null;
    
    PVector offset = landed.getPosition().copy().sub(launchObject.getPosition());
    landed.setVelocity(offset.setMag(1 + launchObject.getRadius()).add(launchObject.getVelocity()));
    landed.onTakeoff();
    landed.update();
    addObject(landed);
    landed = null;
    return true;
  }
}
