class Projectile extends SpaceObject {
  // Default settings
  private final double DEF_MASS = 1000;
  private final int DEF_RADIUS = 2;
  private final int DEF_SPEED = 7;

  private SpaceObject parent;
  private String name;
  private double mass;
  private float radius;
  private color c;

  /**
  *  Default constructor for planets
  */
  
  public Projectile(SpaceObject parent, PVector position, PVector heading, PVector velocity, color c) {
    this.parent = parent;
    this.name = "";
    this.mass = DEF_MASS;
    this.radius = DEF_RADIUS;
    this.position = position;
    this.velocity = heading.setMag(DEF_SPEED).add(velocity);
    this.c = c;
  }
  
  // Draws the projectile
  void draw() {
    stroke(this.c);
    fill(0);
    ellipseMode(RADIUS);
    ellipse(position.x, position.y, radius, radius);
  }
  
  void update() {
    position.add(velocity);
  }
  
  SpaceObject getParent() {return parent;}
  
  String getName() { return name; }
  color getColor() { return c; }
  float getRadius() { return radius; }
  double getMass() { return mass; }
  
  void setMass(double mass) { this.mass = mass; }
  void setRadius(float radius) { this.radius = radius; }
  
  boolean collidesWith(SpaceObject s) {
    return s != getParent() && super.collidesWith(s);
  }
  
  boolean shouldDestroy(SpaceObject other) {
    return true;
  }
}  
