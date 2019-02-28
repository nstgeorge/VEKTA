 /**
*  Model for a planet.
*/
class Planet extends SpaceObject {
  // Default Planet settings
  private final float DEF_MASS = 1.989e30;
  private final PVector DEF_POSITION = new PVector(100, 100);
  private final PVector DEF_VELOCITY = new PVector(0,0);
  private final color DEF_COLOR = color(255, 255, 255);
  private final double MIN_SPLIT_RADIUS = 6;
  private final float SPLIT_DISTANCE_SCALE = 1;
  private final float SPLIT_VELOCITY_SCALE = 1;
  private final float SPLIT_MASS_ABSORB = .5; // Below 1.0 to prevent supermassive planets taking over the map
  private String[] nameParts1 = loadStrings("data/text/planet_prefixes.txt"); // Planet name prefixes
  private String[] nameParts2 = concat(loadStrings("data/text/planet_suffixes.txt"), new String[] {""}); // Planet name suffixes
  
  private String name;
  private float mass;
  private float density;
  private color c;
  
  private float radiusCache;
  
  public Planet() {
    mass     = DEF_MASS;
    //radius   = DEF_RADIUS;
    position = DEF_POSITION;
    velocity = DEF_VELOCITY;
    c = DEF_COLOR;
  }
  
  public Planet(float mass, float density, PVector position, PVector velocity, color c) {
    this.name = nameParts1[(int)random(nameParts1.length)] + nameParts2[(int)random(nameParts2.length)];
    this.mass = mass;
    this.density = density;
    this.position = position;
    this.velocity = velocity;
    this.c = c;
    this.updateRadius();
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
  void update() {
    position.add(velocity);
  }
  
  @Override
  boolean collidesWith(SpaceObject s) {
    // TODO: check getParent() once added to SpaceObject
    return getColor() != s.getColor() && super.collidesWith(s);
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
