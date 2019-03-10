package vekta.object.particle;

import processing.core.PVector;
import vekta.object.SpaceObject;

import java.io.Serializable;

import static processing.core.PConstants.DEG_TO_RAD;
import static vekta.Vekta.addObject;
import static vekta.Vekta.v;

/**
 * Abstraction for regularly emitting a specific particle type.
 */
public class ParticleEmitter implements Serializable {
	private final SpaceObject relative;     // SpaceObject to use for relative velocity
	private final PVector offset;        	// Offset from object position
	private final ParticleStyle style;     // Style of emitted particles
	private final float range;             // Range (in radians) of the emitted particles
	private final float speed;             // Speed (in meters/second) of the emitted particles
	private final float emitPerSecond;     // Number of times to emit per second
	private final int particlesPerEmit;    // Particles to emit for every emission

	private float nextEmit;

	public ParticleEmitter(SpaceObject relative, PVector offset, ParticleStyle style, float range, float speed, float emitPerSecond, int particlePerEmit) {
		this.relative = relative;
		this.offset = offset;
		this.style = style;
		this.range = range;
		this.speed = speed;
		this.emitPerSecond = emitPerSecond;
		this.particlesPerEmit = particlePerEmit;
	}

	// TODO: maybe add getters/setters for the emitter properties?

	public void emit(PVector heading) {
		nextEmit = 0;
		for(int i = 0; i < particlesPerEmit; i++) {
			PVector emitPosition = relative.getPosition().add(offset.copy().rotate(heading.heading()));
			PVector emitVelocity = heading.copy().rotate(v.random(-range, range) * DEG_TO_RAD).setMag(speed);
			emitVelocity.add(relative.getVelocity());
			addObject(createParticle(emitPosition, emitVelocity));
		}
	}

	public void update(PVector heading) {
		nextEmit += emitPerSecond/* / v.frameRate*/ / 60F; // Intentionally emit slightly slower when game is lagging
		if(nextEmit >= 1) {
			emit(heading.copy().rotate(v.random(-range, range) * DEG_TO_RAD).setMag(speed));
		}
	}

	// Subclasses can override this for custom behavior
	public Particle createParticle(PVector position, PVector emitVelocity) {
		return new Particle(relative, position, emitVelocity, style);
	}
}
