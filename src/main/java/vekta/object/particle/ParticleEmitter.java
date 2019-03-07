package vekta.object.particle;

import processing.core.PVector;

import static vekta.Vekta.addObject;
import static vekta.Vekta.v;

/**
 * Abstraction for regularly emitting a specific particle type.
 */
public class ParticleEmitter {
	private final ParticleStyle style;    // Style of emitted particles
	private final float range;            // Range (in radians) of the emitted particles
	private final float speed;            // Speed (in meters/second) of the emitted particles
	private final float emitPerSecond;    // Number of times to emit per second
	private final int particlesPerEmit;    // Particles to emit for every emission

	private float nextEmit;

	public ParticleEmitter(ParticleStyle style, float range, float speed, float emitPerSecond, int particlePerEmit) {
		this.style = style;
		this.range = range;
		this.speed = speed;
		this.emitPerSecond = emitPerSecond;
		this.particlesPerEmit = particlePerEmit;
	}

	public ParticleStyle getStyle() {
		return style;
	}

	// TODO: maybe add getters/setters for the emitter properties?

	public void emit(PVector position, PVector velocity) {
		nextEmit = 0;
		for(int i = 0; i < particlesPerEmit; i++) {
			PVector emitVelocity = velocity.copy().rotate((v.random(-1, 1) * range)).mult(speed);
			addObject(createParticle(position, emitVelocity));
		}
	}

	public void update(PVector position, PVector velocity) {
		nextEmit += emitPerSecond/* / v.frameRate*/ / 60F; // Intentionally emit slightly slower when game is lagging
		if(nextEmit >= 1) {
			emit(position, velocity);
		}
	}

	// Subclasses can override this for custom behavior
	public Particle createParticle(PVector position, PVector emitVelocity) {
		return new Particle(position, emitVelocity, getStyle());
	}
}
