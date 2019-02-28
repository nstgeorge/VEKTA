class Projectile extends SpaceObject {
  // Default settings
  private final float DEF_MASS = 1000;
  private final int DEF_RADIUS = 2;
  private final int DEF_SPEED = 7;

  private SpaceObject parent;
  private String name;
  private float mass;
  private float radius;
  private color c;
  
  public Projectile(SpaceObject parent, PVector position, PVector heading, PVector velocity, color c) {
    this.parent = parent;
    this.name = "Projectile";
    this.mass = DEF_MASS;
    this.radius = DEF_RADIUS;
    this.position = position;
    this.velocity = heading.setMag(DEF_SPEED).add(velocity);
    this.c = c;
  }
  
  @Override
  void draw() {
    stroke(this.c);
    fill(0);
    ellipseMode(RADIUS);
    ellipse(position.x, position.y, radius, radius);
  }
  
  @Override
  void update() {
    position.add(velocity);
  }
  
  SpaceObject getParent() {
    return parent;
  }
  
  @Override
  String getName() { return name; }
  @Override
  color getColor() { return c; }
  @Override
  float getRadius() { return radius; }
  @Override
  float getMass() { return mass; }
  
  @Override
  boolean collidesWith(SpaceObject s) {
    return s != getParent() && super.collidesWith(s);
  }
  
  @Override
  boolean shouldDestroy(SpaceObject other) {
    return true;
  }
}  
