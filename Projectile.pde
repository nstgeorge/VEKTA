class Projectile implements SpaceObject {
  // Default settings
  private final double DEF_MASS = 1000;
  private final int DEF_RADIUS = 2;
  private final int DEF_SPEED = 7;

  private SpaceObject parent;
  private int id;
  private String name;
  private double mass;
  private float radius;
  private PVector position;
  private PVector velocity;
  private color c;

  /**
  *  Default constructor for planets
  */
  
  public Projectile(PVector position, PVector heading, PVector velocity, color c) {
    // this.parent = parent;
    id = (int)random(1000, 4000);
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
  
  @Override
  PVector getInfluenceVector(ArrayList<SpaceObject> space) {
    PVector influence = new PVector(0, 0);
    for(int i = 0; i < space.size(); i++) {
      SpaceObject s = space.get(i);
      if(position.dist(s.getPosition()) < MAX_DISTANCE) {
        double r = PVector.dist(position, s.getPosition()) * SCALE;
        if(r == 0) return new PVector(0,0); // If the planet being checked is itself (or directly on top), don't move
        double force = G * ((mass * s.getMass()) / (r * r)); // G defined in orbit
        influence.add(new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag((float)(force / mass)));
      }
    }
    velocity.add(influence);
    return influence;
  }  
  
  void update() {
    position.add(velocity);
  }  
  
  int getID() { return id; }
  String getName() { return name; }
  color getColor() { return c; }
  float getRadius() { return radius; }
  PVector getVelocity() { return velocity; }
  PVector getPosition() { return position; }
  double getMass() { return mass; }
  
  void setMass(double mass) { this.mass = mass; }
  void setRadius(float radius) { this.radius = radius; }
  
  void onDestroy(SpaceObject s){}
  
  PVector addVelocity(PVector add) {
    return velocity.add(add);
  }
  
  boolean collidesWith(SpaceObject s) {
    double r = PVector.dist(position, s.getPosition());
    if(r < (getRadius() + s.getRadius())) return true;
    return false;
  }  
}  
