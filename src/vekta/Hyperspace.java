package vekta;

import processing.core.PVector;

import static vekta.Vekta.getInstance;

class Hyperspace {
	private PVector origin;
	private float accel;
	private Particle[] particles;
	private final float INIT_VELOCITY = .2F;

	public Hyperspace(PVector origin, float accel, int particleNum) {
		Vekta v = getInstance();
		this.origin = origin;
		this.accel = accel;
		particles = new Particle[particleNum];
		// Generate a new set of particles all at once
		int i = 0;
		while(i < particleNum) {
			particles[i] = newParticle(new PVector(v.random(0, v.width), v.random(0, v.height)));
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
		Vekta v = getInstance();
		for(int i = 0; i < particles.length; i++) {
			Particle p = particles[i];
			particles[i].update();
			if(p.getLoc().x > v.width + 300 || p.getLoc().y > v.height + 200 || p.getLoc().x < -300 || p.getLoc().y < -200) {
				particles[i] = newParticle();
			}
		}
	}
}  
