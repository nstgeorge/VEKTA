package vekta.particle;

import processing.core.PVector;
import vekta.context.World;

import static vekta.Vekta.*;

public class ParticleSystem {

    private PVector position;       // Position of the emitter
    private PVector velocity;       // Average velocity of emitted particles (modified within range)
    private float range;            // Range (in radians) of the emitted particles
    private float frequency;        // Emittances per second (that isn't the right word)
    private int particlePerEmit;    // Particles to emit for every emittance
    private int beginColor;         // First range indicator for colored particles
    private int endColor;           // Second range indicator for colored particles

    private float nextEmit;

    public ParticleSystem(PVector position, PVector velocity, float range, float frequency, int particlePerEmit, int beginColor, int endColor) {
        this.position = position;
        this.velocity = velocity;
        this.range = range;
        this.frequency = frequency;
        this.particlePerEmit = particlePerEmit;
        this.beginColor = beginColor;
        this.endColor = endColor;

        nextEmit = -1;
    }

    public void emit() {
        if(getContext() instanceof World) {
            for(int i = 0; i < particlePerEmit; i++) {
                PVector newVelocity = new PVector();
                PVector.fromAngle(velocity.heading() + (v.random(-1, 1) * range), newVelocity);
                newVelocity.setMag(velocity.mag());
                register(new Particle(position, newVelocity, v.lerpColor(beginColor, endColor, v.random(1)), .1F, 2000));
            }
            nextEmit = v.frameCount + ((1/frequency) * v.frameRate);
        }
    }

    public void loopEmit() {
        if(v.frameCount > nextEmit) {
            emit();
        }
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }
}
