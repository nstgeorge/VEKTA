/**
*  Model for a planet.
*/
class Planet extends SpaceObject {
  private final double MIN_SPLIT_RADIUS = 6;
  private final float SPLIT_DISTANCE_SCALE = 1;
  private final float SPLIT_VELOCITY_SCALE = 1;
  private final float SPLIT_MASS_ABSORB = .5; // Below 1.0 to prevent supermassive planets taking over the map
  private final float MIN_LANDING_SPEED = .2;
  
  private final String name;
  private float mass;
  private final float density;
  private final color c;
  
  private float radiusCache;
  
  private final LandingSite site;
  
  public Planet(float mass, float density, PVector position, PVector velocity, color c) {
    super(position, velocity);
    this.name = planetNamePrefixes[(int)random(planetNamePrefixes.length)] + planetNameSuffixes[(int)random(planetNameSuffixes.length)];
    this.mass = mass;
    this.density = density;
    this.c = c;
    
    site = new LandingSite(this);
    updateRadius();
  }
  
  @Override
  void draw() {
    stroke(this.c);
    fill(0);
    ellipseMode(RADIUS);
    float radius = getRadius();
    ellipse(position.x, position.y, radius, radius);
  }
  
  @Override
  boolean collidesWith(SpaceObject s) {
    // TODO: check getParent() once added to SpaceObject
    return getColor() != s.getColor() && super.collidesWith(s);
  }
  
  @Override
  void onCollide(SpaceObject s) {
    // Attempt landing
    if(s instanceof Spaceship) { // TODO: create `Lander` interface for event handling
      Spaceship ship = (Spaceship)s;
      if(ship.isLanding()) {
        float magSq = velocity.copy().sub(s.getVelocity()).magSq();
        if(magSq > MIN_LANDING_SPEED * MIN_LANDING_SPEED && site.land(ship)) {
          return;
        }
      }
    }
    // Oof
    super.onCollide(s);
  }
  
  @Override
  void onDestroy(SpaceObject s) {
    //println("Planet destroyed with radius: " + getRadius());
    
    // If sufficiently large, split planet in half
    if(getRadius() >= MIN_SPLIT_RADIUS) {
      float newMass = getMass() / 2;
      float newRadius = getRadius() / pow(2, 1/3);
      
      // Use mass-weighted collision velocity for base debris velocity
      float xWeight = getVelocity().x * getMass() + s.getVelocity().x * s.getMass();
      float yWeight = getVelocity().y * getMass() + s.getVelocity().y * s.getMass();
      float massSum = getMass() + s.getMass();
      PVector newVelocity = new PVector(xWeight / massSum, yWeight / massSum);
      
      PVector base = getPosition().copy().sub(s.getPosition()).normalize().rotate(90);
      PVector offset = base.copy().mult(newRadius * SPLIT_DISTANCE_SCALE);
      PVector splitVelocity = base.copy().mult(SPLIT_VELOCITY_SCALE);
      Planet a = new Planet(newMass, getDensity(), getPosition().copy().add(offset), newVelocity.copy().add(splitVelocity), getColor());
      Planet b = new Planet(newMass, getDensity(), getPosition().copy().sub(offset), newVelocity.copy().sub(splitVelocity), getColor());
      if(!s.collidesWith(a)) {
        mass -= a.mass;
        addObject(a);
      }
      if(!s.collidesWith(b)) {
        mass -= b.mass;
        addObject(b);
      }
    }
    
    // If this is a planetary collision, add some additional mass to the other planet
    if(mass > 0 && s instanceof Planet) {
      Planet p = (Planet)s;
      p.setMass(p.getMass() + mass * SPLIT_MASS_ABSORB);
    }
    
    // TODO: move this eventually
    // If something landed on this planet, destroy it as well
    SpaceObject landed = site.landed;
    if(landed != null) {
      landed.onDestroy(s);
    }
  }
  
  @Override
  float getMass() {
    return mass;
  }
  
  @Override
  String getName() {
    return name;
  }  
  
  void setMass(float mass) {
    this.mass = mass;
    updateRadius();
  }
  
  @Override
  float getRadius() {
    return radiusCache;
  }
  
  void updateRadius() {
    radiusCache = pow(getMass() / getDensity(), (float)1/3) / SCALE;
  }
  
  @Override
  color getColor() {
    return c;
  }
  
  float getDensity() {
    return density;
  }
  
  // Deprecated: not being used currently
  /**
    Returns a PVector position that stays bounded to the screen.
  */
  /*
  private PVector setPlanetLoc(PVector position, PVector velocity) {
    if(position.x + velocity.x <= 0 && position.y + velocity.y <= 0)
      return new PVector(width, height);
      
     else if(position.x + velocity.x <= 0)
       return new PVector(width, (position.y + velocity.y) % height);
       
     else if(position.y +velocity.y <= 0)
       return new PVector((position.x + velocity.x) % width, height);
       
     else
       return new PVector((position.x + velocity.x) % width, (position.y + velocity.y) % height);
  }*/
}
