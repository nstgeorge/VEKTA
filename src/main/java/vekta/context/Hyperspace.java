package vekta.context;

import processing.core.PVector;

import java.io.Serializable;

import static vekta.Vekta.*;

public class Hyperspace implements Serializable {
	private final PVector origin;
	private final float accel;
	private final HyperspaceParticle[] particles;
	private final float INIT_VELOCITY = .2F;

	public Hyperspace(PVector origin, float accel, int particleNum) {
		this.origin = origin;
		this.accel = accel;
		particles = new HyperspaceParticle[particleNum];
		// Generate a new set of particles all at once
		int i = 0;
		while(i < particleNum) {
			particles[i] = newParticle(new PVector(v.random(0, v.width), v.random(0, v.height)));
			i++;
		}
	}

	public HyperspaceParticle newParticle(PVector loc) {
		// Create a new random PVector
		PVector accelVector = loc.copy().sub(origin);
		accelVector.setMag(accel);
		return new HyperspaceParticle(loc.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
	}

	public HyperspaceParticle newParticle() {
		// Create a new random PVector
		PVector accelVector = PVector.random2D();
		accelVector.setMag(accel);
		return new HyperspaceParticle(origin.copy(), accelVector.copy().setMag(INIT_VELOCITY), accelVector.copy());
	}

	public void render() {
		//		v.hint(ENABLE_DEPTH_TEST);
		update();
		for(HyperspaceParticle p : particles) {
			p.render();
		}
		//		v.hint(DISABLE_DEPTH_TEST);
	}

	public void update() {
		for(int i = 0; i < particles.length; i++) {
			HyperspaceParticle p = particles[i];
			particles[i].update();
			if(p.getPosition().x > v.width + 300 || p.getPosition().y > v.height + 200 || p.getPosition().x < -300 || p.getPosition().y < -200) {
				particles[i] = newParticle();
			}
		}
	}

	private static class HyperspaceParticle {
		private final PVector loc;
		private final PVector accel;
		private final PVector velocity;
		private final float dist;
		private float time;

		public HyperspaceParticle(PVector loc, PVector initVelocity, PVector accel) {
			this.loc = loc;
			this.accel = accel;
			dist = v.random(.2F, 2);
			this.velocity = initVelocity.mult(dist);
			time = 0;
		}

		public void render() {
			v.noFill();
			v.stroke(Math.max((((time * 10) - 255) - (dist * 100)) / 4, 0));
			v.strokeWeight(2);
			v.line(loc.x, loc.y, loc.x - (velocity.x * time / 9), loc.y - (velocity.y * time / 9));
		}

		public void update() {
			loc.add(velocity);
			velocity.add(accel);
			time++;
		}

		public PVector getPosition() {
			return loc;
		}
	}
}  
