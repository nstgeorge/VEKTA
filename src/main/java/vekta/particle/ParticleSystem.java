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
    private Particle[] particles;
    private int particleCount;

    public ParticleSystem(PVector position, PVector velocity, float range, float frequency, int particlePerEmit, int beginColor, int endColor) {
        this.position = position;
        this.velocity = velocity;
        this.range = range;
        this.frequency = frequency;
        this.particlePerEmit = particlePerEmit;
        this.beginColor = beginColor;
        this.endColor = endColor;

        nextEmit = -1;
        particleCount = 0;
        particles = new Particle[particlePerEmit * (int)Math.ceil(frequency) * 100]; // Safe enough guess for maximum particles
    }

    public void emit() {
        if(getContext() instanceof World) {
            World world = getWorld();
            for(int i = 0; i < particlePerEmit; i++) {
                PVector newVelocity = new PVector();
                PVector.fromAngle(velocity.heading() + (v.random(-1, 1) * range), newVelocity);
                newVelocity.setMag(velocity.mag());
                particles[particleCount] = new Particle(position, newVelocity, v.lerpColor(beginColor, endColor, v.random(1)), 0.01F, 2000);
                world.addObject(particles[particleCount]);
                particleCount++;
            }
            nextEmit = v.frameCount + ((1/frequency) * v.frameRate);
        }
        cleanParticleArray();
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

    private void cleanParticleArray() {
        if(getContext() instanceof World) {
            World world = getWorld();
            for (int i = 0; i < particleCount; i++) {
                if (particles[i] != null && particles[i].dead()) {
                    world.removeObject(particles[i]);
                    particles[i] = null;
                    // Collapse array
                    for(int j = particleCount - 1; j > i; j--) {
                        particles[j] = particles[j - 1];
                    }
                    particleCount--;
                }
            }
        }
    }
}
