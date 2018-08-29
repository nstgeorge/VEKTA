 /**
*  Model for a planet.
*/
class Planet implements SpaceObject {
  // Default Planet settings
  private final double DEF_MASS = 1.989 * Math.pow(10, 30);
  private final int DEF_RADIUS = 15;
  private final PVector DEF_POSITION = new PVector(100, 100);
  private final PVector DEF_VELOCITY = new PVector(0,0);
  private final color DEF_COLOR = color(255, 255, 255);
  private String[] nameParts1 = {  // First parts of the name of a planet
    "Giga", "Atla", "Exo", "Zori", "Era", "Volta", "Dene", "Julu", "Poke", "Sala", "Huno", "Yeba", "Satu", "Plu", "Mercu", "Luki", "Pola", "Crato"
  };
  private String[] nameParts2 = {
    "dan", "san", "jor", "zed", "ranth", "ka", "", "th", "rn", "to", "krith", "n", "s", "sol", "deth", "rat", "kor", "k", "shyyyk", "tron", "don", "saur", "ris"
  };
  
  private String name;
  private int id;
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
  
  public Planet(int id, double mass, float radius, int x, int y, float xVelocity, float yVelocity, color c) {
    
    this.name = nameParts1[(int)random(nameParts1.length)] + nameParts2[(int)random(nameParts2.length)];
    this.id = id;
    this.mass = mass;
    this.radius = radius;
    position = new PVector(x, y);
    velocity = new PVector(xVelocity, yVelocity);
    this.c = c;
  }
  
  // Draws the planet
  void draw() {
    stroke(this.c);
    fill(0);
    ellipseMode(RADIUS);
    if(id == 1) System.out.println(velocity);
    ellipse(position.x, position.y, radius, radius);
  }
  
  void update() {
    position.add(velocity);
  }  
  
  /**
    Calculates influence of this Planet *from* another object in space.
  */
  PVector getInfluenceVector(ArrayList<SpaceObject> space) {
    
    for(int i = 0; i < space.size(); i++) {
      PVector influence = new PVector(0, 0);
      SpaceObject s = space.get(i);
      float dist = position.dist(s.getPosition()); 
      if(dist < MAX_DISTANCE) {
        double r = dist * SCALE;
        if(r == 0) return new PVector(0,0); // If the planet being checked is itself (or directly on top), don't move
        double force = G * ((mass * s.getMass()) / (r * r)); // G defined in orbit
        influence.add(new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag((float)(force / mass)));
      }
      velocity.add(influence);
    }
    return new PVector();
  }  
  
  /**
    Checks if this planet collides with another planet
  */
  boolean collidesWith(SpaceObject s) {
    double r = PVector.dist(position, s.getPosition());
    if(r < (getRadius() + s.getRadius())) return true;
    return false;
  }  
  
  @Override
  double getMass() {
    return mass;
  }
  
  int getID() { return id; }
  
  String getName() {
    return name;
  }  
  
  // Mass setter
  void setMass(int mass) {
    this.mass = mass;
  }
  
  @Override
  float getRadius() {
    return radius;
  }
  
  // Radius setter
  void setRadius(int radius) {
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
