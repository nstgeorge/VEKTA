package vekta.particle;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.context.World;
import vekta.object.SpaceObject;

import static vekta.Vekta.*;

public class Particle extends SpaceObject {

    private float drag;          // Reduce the speed by this amount every draw cycle
    private int lifetime;        // How long the particle should be visible for (ms)
    private long initTime;       // Time of particle creation

    public Particle(PVector position, PVector velocity, int color, float drag, int lifetime) {
        super(position.copy(), velocity.copy(), color);
        this.drag = drag;
        this.lifetime = (int)(lifetime * v.frameRate / 1000);
        initTime = v.frameCount;
    }

    public void draw() {
        PVector currentVelocity = super.getVelocity();
        //int currentColor = super.getColor();
        //int newColor = v.lerpColor(currentColor, 0, (float)(initTime + System.currentTimeMillis()) / (initTime + lifetime));

        super.subVelocity(currentVelocity.mult(drag));

        if(dead()) {
            if(getContext() instanceof World) {
                getWorld().removeObject(this);
            }
        }
        // Note: Nothing is drawn here because particles are nothing but trails. This just sets the color and velocity of the particle's head.
    }

    /**
     * Get the lifetime of the particle
     * @return particle lifetime
     */
    public boolean dead() { return initTime + lifetime <= v.frameCount; }

    @Override
    public String getName() {
        return "Particle";
    }

    @Override
    public float getMass() {
        return .1F; // Prevents divide by zero
    }

    @Override
    public float getRadius() {
        return .001F; // Practically not there. Prevents collision issues
    }

    @Override
    public RenderLevel getRenderLevel() {
        return RenderLevel.SHIP;
    }

    @Override
    public float getSpecificHeat() {
        return 1;
    }
}
