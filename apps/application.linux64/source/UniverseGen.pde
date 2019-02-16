class UniverseGen {
  int size;
  int density;
  ArrayList<Planet> space = new ArrayList<Planet>();
  
  int currentID;
  
  public UniverseGen(int size, int density) {
    this.size = size;
    this.density = density;
    currentID = 0;
  }
  
  public ArrayList<Planet> generate() {
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
      ArrayList<Planet> system = createSystem((int)coords[0], (int)coords[1]);
      for(int j = 0; j < system.size(); j++) {
        space.add(system.get(j));
      }
    }
    return space;
  }
  
  private ArrayList<Planet> createSystem(int x, int y) {
    ArrayList<Planet> system = new ArrayList<Planet>();
    // Create the center body
    double power = Math.pow(10, random(28, 31));
    double centerMass = random(0.8, 4) * power;
    float radius = (float)(random(3, 6) * (centerMass / power));
    system.add(new Planet(
      currentID,
      centerMass, // Mass
      radius,   // Radius
      x, y,  // Coords
      0, 0,  // Velocity
      color(random(100, 255), random(100, 255), random(100, 255))
    ));
    currentID++;
    
    // Generate planets around body
    int planets = (int)random(1, 8);
    for(int i = 0; i <= planets; i++) {
      float radiusLoc = (float)(random(600, 2000) * SCALE);
      float velocity = sqrt((float)(G * centerMass / radiusLoc));
      double mass = random(0.8, 4) * power;
      float radiusSize = (float)(random(2, 5) * (mass / power));
      System.out.println(radiusSize);
      system.add(new Planet(
        currentID,
        mass, // Mass
        radiusSize,   // Radius
        (int)(x + radiusLoc), y,  // Coords
        0, (float)(velocity),  // Velocity
        color(random(100, 255), random(100, 255), random(100, 255))
      ));
      currentID++;
    }  
    return system;
  }
  
  private float[] generateCoordinates(float max) {
    float[] retVal = {random(max), random(max)};
    return retVal;
  }  
}  
