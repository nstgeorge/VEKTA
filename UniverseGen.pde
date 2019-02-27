static final int MIN_SPAWN_DISTANCE = 500;

class UniverseGen {
  int size;
  int density;
  List<Planet> space = new ArrayList<Planet>();
  
  public UniverseGen(int size, int density) {
    this.size = size;
    this.density = density;
  }
  
  public List<Planet> generate() {
    for(int i = 0; i < density; i++) {
      space.addAll(createSystem(generateCoordinates(size)));
    }
    return space;
  }
  
  private List<Planet> createSystem(PVector pos) {
    List<Planet> system = new ArrayList<Planet>();
    // Create the center body
    double power = Math.pow(10, random(28, 31));
    double centerMass = random(0.8, 4) * power;
    float radius = (float)(random(3, 6) * (centerMass / power));
    system.add(new Planet(
      centerMass, // Mass
      radius,   // Radius
      pos,  // Coords
      new PVector(),  // Velocity
      color(random(100, 255), random(100, 255), random(100, 255))
    ));
    
    // Generate planets around body
    int planets = (int)random(1, 8);
    for(int i = 0; i <= planets; i++) {
      float radiusLoc = (float)(random(600, 2000) * SCALE);
      float speed = sqrt((float)(G * centerMass / radiusLoc));
      double mass = random(0.8, 4) * power;
      float radiusSize = (float)(random(2, 5) * (sqrt((float)mass) / power));
      system.add(new Planet(
        mass, // Mass
        radiusSize,   // Radius
        new PVector(pos.x + radiusLoc, pos.y),  // Coords
        new PVector(0, speed),  // Velocity
        color(random(100, 255), random(100, 255), random(100, 255))
      ));
    }  
    return system;
  }
  
  private PVector generateCoordinates(float max) {
    return PVector.random2D().mult(MIN_SPAWN_DISTANCE + random(max - MIN_SPAWN_DISTANCE));
  }  
}  
