static final int MIN_SPAWN_DISTANCE = 500;

class UniverseGen {
  int size;
  int density;
  
  public UniverseGen(int size, int density) {
    this.size = size;
    this.density = density;
  }
  
  public List<Planet> generate() {
    List<Planet> objects = new ArrayList<Planet>();
    for(int i = 0; i < density; i++) {
      objects.addAll(createSystem(generateCoordinates(size)));
    }
    return objects;
  }
  
  private List<Planet> createSystem(PVector pos) {
    List<Planet> system = new ArrayList<Planet>();
    // Create the center body
    double power = Math.pow(10, random(28, 31));
    double centerMass = random(0.8, 4) * power;
    float centerDensity = random(1.0, 2);
    system.add(new Planet(
      centerMass, // Mass
      centerDensity,   // Radius
      pos,  // Position
      new PVector(),  // Velocity
      color(random(100, 255), random(100, 255), random(100, 255))
    ));
    
    // Generate planets around body
    int planets = (int)random(1.0, 8);
    for(int i = 0; i <= planets; i++) {
      float radiusLoc = random(100, 2000);
      float speed = sqrt((float)(G * centerMass / radiusLoc)) / (float)SCALE;
      double mass = random(0.8, 4) * power;
      float density = random((float)4, 8);
      system.add(new Planet(
        mass, // Mass
        density,   // Density
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
