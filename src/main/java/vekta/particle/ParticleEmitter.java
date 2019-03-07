package vekta.particle;

import processing.core.PVector;

import static vekta.Vekta.addObject;
import static vekta.Vekta.v;

/**
 * Abstraction for regularly emitting a specific particle type.
 */
public class ParticleEmitter {
	private final float range;			// Range (in radians) of the emitted particles
	private final int emitTime;			// Number of frames between emissions
	private final int particlesPerEmit;	// Particles to emit for every emission
	private final int beginColor;		// First range indicator for colored particles
	private final int endColor;			// Second range indicator for colored particles

	private int nextEmit;

	public ParticleEmitter(float range, int emitTime, int particlePerEmit, int beginColor, int endColor) {
		this.range = range;
		this.emitTime = emitTime;
		this.particlesPerEmit = particlePerEmit;
		this.beginColor = beginColor;
		this.endColor = endColor;
	}

	public void emit(PVector position, PVector velocity) {
		nextEmit = 0;
		for(int i = 0; i < particlesPerEmit; i++) {
			addObject(createParticle(position, velocity));
		}
	}

	public Particle createParticle(PVector position, PVector velocity) {
		PVector newVelocity = velocity.copy().rotate((v.random(-1, 1) * range));
		return new Particle(position, newVelocity, v.lerpColor(beginColor, endColor, v.random(1)), 0.01F, 100);
	}

	public void update(PVector position, PVector velocity) {
		if(nextEmit++ >= emitTime) {
			emit(position, velocity);
		}
	}
}
