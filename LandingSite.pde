/**
  A landing site for one spacecraft-like object.
  State management for landing sites should be handled or proxied through this class.
*/
class LandingSite {
  // TODO: convert this to an interface or abstract class if necessary
  
  private final SpaceObject parent;
  
  private Spaceship landed;
  
  private Menu menu;
  
  public LandingSite(SpaceObject parent) {
    this.parent = parent;
  }
  
  public SpaceObject getParent() {
    return parent;
  }
  
  public boolean land(Spaceship ship) {
    if(landed != null) {
      return false;
    }
    
    landed = ship;
    removeObject(ship);
    menu = new Menu(new LandingMenuHandle(this));
    menu.addItem(new BuyItem());
    menu.addItem(new SellItem());
    menu.addItem(new InfoItem());
    menu.addItem(new TakeoffItem(this));
    openMenu(menu); // Push to global menu stack
    return true;
  }
  
  public boolean takeoff() {
    if(landed == null) {
      return false;
    }
    
    closeMenu(menu); // Close menu associated with this landing site
    menu = null;
    
    PVector offset = landed.getPosition().copy().sub(getParent().getPosition());
    landed.setVelocity(offset.setMag(getLaunchSpeed()).add(getParent().getVelocity()));
    landed.onTakeoff();
    landed.update();
    addObject(landed);
    landed = null;
    return true;
  }
  
  /**
  Compute the launch speed (subject to rebalancing)
  */
  float getLaunchSpeed() {
    return 1 + parent.getRadius() / 2;
  }
}
