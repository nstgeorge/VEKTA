class Particle {
  private PVector loc;
  private PVector accel;
  private PVector velocity;
  private float dist;
  private float time;
  public Particle(PVector loc, PVector initVelocity, PVector accel) {
    this.loc = loc;
    this.accel = accel;
    dist = random(.2, 2);
    this.velocity = initVelocity.mult(dist);
    time = 0;
  }
  
  void render() {
    fill(0);
    stroke(Math.max((((time * 10) - 255) - (dist * 100)) / 4, 0));
    line(loc.x, loc.y, loc.x-(velocity.x * time/9), loc.y-(velocity.y * time/9));
  }
  
  void update() {
    loc.add(velocity);
    velocity.add(accel);
    time++;
  }
  
  PVector getLoc() {
    return loc;
  }  
}
