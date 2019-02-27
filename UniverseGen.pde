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
      float[] coords = {0, 0};
      boolean unique = false;
      
      while(!unique) {
        coords = generateCoordinates(size);
        unique = true;
        for(Planet s : space) {
          if(s.getPosition().x == coords[0] && s.getPosition().y == coords[1]) {
            unique = false;
          }
        }
      }
      List<Planet> system = createSystem((int)coords[0], (int)coords[1]);
      space.addAll(system);
    }
    return space;
  }
  
  private List<Planet> createSystem(int x, int y) {
    List<Planet> system = new ArrayList<Planet>();
    // Create the center body
    double power = Math.pow(10, random(28, 31));
    double centerMass = random(0.8, 4) * power;
    float radius = (float)(random(3, 6) * (centerMass / power));
    system.add(new Planet(
      centerMass, // Mass
      radius,   // Radius
      x, y,  // Coords
      0, 0,  // Velocity
      color(random(100, 255), random(100, 255), random(100, 255))
    ));
    
    // Generate planets around body
    int planets = (int)random(1, 8);
    for(int i = 0; i <= planets; i++) {
      float radiusLoc = (float)(random(600, 2000) * SCALE);
      float velocity = sqrt((float)(G * centerMass / radiusLoc));
      double mass = random(0.8, 4) * power;
      float radiusSize = (float)(random(2, 5) * (sqrt((float)mass) / power));
      system.add(new Planet(
        mass, // Mass
        radiusSize,   // Radius
        (int)(x + radiusLoc), y,  // Coords
        0, (float)(velocity),  // Velocity
        color(random(100, 255), random(100, 255), random(100, 255))
      ));
    }  
    return system;
  }
  
  private float[] generateCoordinates(float max) {
    float[] retVal = {random(max) - max / 2, random(max) - max / 2};
    return retVal;
  }  
}  
