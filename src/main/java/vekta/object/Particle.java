package vekta.object;

import processing.core.PVector;
import vekta.Vekta;

import static vekta.Vekta.getInstance;

public class Particle {
	private PVector loc;
	private PVector accel;
	private PVector velocity;
	private float dist;
	private float time;

	public Particle(PVector loc, PVector initVelocity, PVector accel) {
		this.loc = loc;
		this.accel = accel;
		dist = getInstance().random(.2F, 2);
		this.velocity = initVelocity.mult(dist);
		time = 0;
	}

	public void render() {
		Vekta v = getInstance();
		v.fill(0);
		v.stroke(Math.max((((time * 10) - 255) - (dist * 100)) / 4, 0));
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
