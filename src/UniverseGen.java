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
    float order = random(29, 32);
    // Create the center body
    float centerPower = (float)pow(10, order);
    float centerMass = random(0.8, 4) * centerPower;
    float centerDensity = random(1, 2);
    system.add(setupPlanet(new Planet(
      centerMass, // Mass
      centerDensity,   // Radius
      pos,  // Position
      new PVector(),  // Velocity
      color(random(100, 255), random(100, 255), random(100, 255))
    )));
    
    // Generate planets around body
    int planets = (int)random(1, 8);
    for(int i = 0; i <= planets; i++) {
      float power = (float)Math.pow(10, order - 1);
      float radiusLoc = random(100, 2000);
      float speed = sqrt(G * centerMass / radiusLoc) / SCALE;
      float mass = random(0.05, 0.5) * power;
      float density = random(4, 8);
      float angle = random(360);
      system.add(setupPlanet(new Planet(
        mass, // Mass
        density,   // Density
        new PVector(radiusLoc, 0).rotate(angle).add(pos),  // Coords
        new PVector(0, speed).rotate(angle),  // Velocity
        color(random(100, 255), random(100, 255), random(100, 255))
      )));
    }  
    return system;
  }
  
  private Planet setupPlanet(Planet planet) {
    LandingSite site = planet.getLandingSite();
    Inventory inv = site.getInventory();
    Map<Item, Integer> offers = site.getOffers();
    
    inv.add((int)random(10, 500));
    int itemCt = (int)random(1, 4);
    for(int i = 0; i < itemCt; i++) {
      Item item = new Item(generateItemName(), ItemType.COMMON);
      inv.add(item);
      int price = (int)random(1, 20);
      offers.put(item, price);
    }
    
    return planet;
  }
  
  private PVector generateCoordinates(float max) {
    return PVector.random2D().mult(MIN_SPAWN_DISTANCE + random(max - MIN_SPAWN_DISTANCE));
  }  
}  
