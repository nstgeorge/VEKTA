 /**
*  Model for a planet.
*/
class Planet extends SpaceObject {
  // Default Planet settings
  private final double DEF_MASS = 1.989 * Math.pow(10, 30);
  private final int DEF_RADIUS = 15;
  private final PVector DEF_POSITION = new PVector(100, 100);
  private final PVector DEF_VELOCITY = new PVector(0,0);
  private final color DEF_COLOR = color(255, 255, 255);
  private final float SPLIT_MASS_DECAY = .2;
  private final double MIN_SPLIT_RADIUS = 6;
  private final float SPLIT_DISTANCE_SCALE = 3;
  private final float SPLIT_VELOCITY_SCALE = 5;
  private String[] nameParts1 = loadStrings("data/text/planet_prefixes.txt"); // Planet name prefixes
  private String[] nameParts2 = concat(loadStrings("data/text/planet_suffixes.txt"), new String[] {""}); // Planet name suffixes
  
  private String name;
  private double mass;
  private float radius;
  private PVector position;
  private PVector velocity;
  private color c;

  /**
  *  Default constructor for planets
  */
  public Planet() {
    mass     = DEF_MASS;
    radius   = DEF_RADIUS;
    position = DEF_POSITION;
    velocity = DEF_VELOCITY;
    c = DEF_COLOR;
  }
  
  public Planet(double mass, float radius, PVector position, PVector velocity, color c) {
    
    this.name = nameParts1[(int)random(nameParts1.length)] + nameParts2[(int)random(nameParts2.length)];
    this.mass = mass;
    this.radius = radius;
    this.position = position;
    this.velocity = velocity;
    this.c = c;
  }
  
  // Draws the planet
  void draw() {
    stroke(this.c);
    fill(0);
    ellipseMode(RADIUS);
    //if(id == 1) System.out.println(velocity);
    ellipse(position.x, position.y, radius, radius);
  }
  
  void update() {
    position.add(velocity);
  }
  
  void onDestroy(SpaceObject s) {
    //println("Planet destroyed with radius: " + getRadius());
    
    // Add this object's mass and radius to the mass and radius of the destroying object
    if(!(s instanceof Projectile)) {
      s.setMass(s.getMass() + mass * SPLIT_MASS_DECAY);
      s.setRadius(sqrt(s.getRadius() * s.getRadius() + radius * radius * SPLIT_MASS_DECAY * SPLIT_MASS_DECAY));
    } else {
      if(getRadius() >= MIN_SPLIT_RADIUS) {
        // Split large planet
        double newMass = getMass() / 2;
        float newRadius = getRadius() / sqrt(2);
        PVector offset = PVector.random2D().mult(newRadius * SPLIT_DISTANCE_SCALE);
        PVector splitVelocity = /*PVector.random2D()*/getPosition().copy().sub(s.getPosition()).rotate(90).normalize().mult(SPLIT_VELOCITY_SCALE);
        // Note that the planet ids are overwritten by addObject(..) logic
        Planet a = new Planet(newMass, newRadius, getPosition().copy().add(offset), getVelocity().copy().add(splitVelocity), getColor());
        Planet b = new Planet(newMass, newRadius, getPosition().copy().sub(offset), getVelocity().copy().sub(splitVelocity), getColor());
        if(!s.collidesWith(a)) {
          addObject(a);
        }
        if(!s.collidesWith(b)) {
          addObject(b);
        }
      }
    }
  }
  
  @Override
  double getMass() {
    return mass;
  }
  
  String getName() {
    return name;
  }  
  
  // Mass setter
  void setMass(double mass) {
    this.mass = mass;
  }
  
  @Override
  float getRadius() {
    return radius;
  }
  
  // Radius setter
  void setRadius(float radius) {
    this.radius = radius;
  }
  
  color getColor() {
    return c;
  }  
  
  void setColor(color c) {
    this.c = c;
  }  
  
  @Override
  PVector getPosition() {
    return position.copy();
  }
  
  @Override
  PVector getVelocity() {
    return velocity.copy();
  }
  
  @Override
  PVector addVelocity(PVector add) {
    return velocity.add(add);
  }
  
  /**
    Returns a PVector position that stays bounded to the screen.
  */
  private PVector setPlanetLoc(PVector position, PVector velocity) {
    if(position.x + velocity.x <= 0 && position.y + velocity.y <= 0)
      return new PVector(width, height);
      
     else if(position.x + velocity.x <= 0)
       return new PVector(width, (position.y + velocity.y) % height);
       
     else if(position.y +velocity.y <= 0)
       return new PVector((position.x + velocity.x) % width, height);
       
     else
       return new PVector((position.x + velocity.x) % width, (position.y + velocity.y) % height);
  }
}
