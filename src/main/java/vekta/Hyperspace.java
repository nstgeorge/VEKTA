package vekta;

import processing.core.PVector;
import vekta.object.Particle;

import static vekta.Vekta.getInstance;

public class Hyperspace {
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

	public Particle newParticle(PVector loc) {
		// Create a new random PVector
		PVector accelVector = loc.copy().sub(origin);
		accelVector.setMag(accel);
		return new Particle(loc.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
	}

	public Particle newParticle() {
		// Create a new random PVector
		PVector accelVector = PVector.random2D();
		accelVector.setMag(accel);
		return new Particle(origin.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
	}

	public void render() {
		update();
		for(Particle p : particles) {
			p.render();
		}
	}

	public void update() {
		Vekta v = getInstance();
		for(int i = 0; i < particles.length; i++) {
			Particle p = particles[i];
			particles[i].update();
			if(p.getPosition().x > v.width + 300 || p.getPosition().y > v.height + 200 || p.getPosition().x < -300 || p.getPosition().y < -200) {
				particles[i] = newParticle();
			}
		}
	}
}  
