package vekta;

class Hyperspace {
  private PVector origin;
  private float accel;
  private Particle[] particles;
  private final float INIT_VELOCITY = .2;
  
  public Hyperspace(PVector origin, float accel, int particleNum) {
      this.origin = origin;
      this.accel = accel;
      particles = new Particle[particleNum];
      // Generate a new set of particles all at once
      int i = 0;
      while(i < particleNum) {
        particles[i] = newParticle(new PVector(random(0, width), random(0, height)));
        i++;
      }  
  }
  
  Particle newParticle(PVector loc) {
    // Create a new random PVector
      PVector accelVector = loc.copy().sub(origin);
      accelVector.setMag(accel);
      return new Particle(loc.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
  }
  
  Particle newParticle() {
    // Create a new random PVector
      PVector accelVector = PVector.random2D();
      accelVector.setMag(accel);
      return new Particle(origin.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
  }
  
  void render() {
    update();
    for(Particle p : particles) {
      p.render();
    }
  }
  
  void update() {
    for(int i = 0; i < particles.length; i++) {
      Particle p = particles[i];
      particles[i].update();
      if(p.getLoc().x > width + 300 || p.getLoc().y > height + 200 || p.getLoc().x < -300 || p.getLoc().y < -200) {
        particles[i] = newParticle();
      }
    }
  }
}  
