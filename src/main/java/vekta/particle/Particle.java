package vekta.particle;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.SpaceObject;

public class Particle extends SpaceObject {

	private float drag;          // Reduce the speed by this amount every draw cycle
	private int lifetime;        // How long the particle should be visible for (ms)

	public Particle(PVector position, PVector velocity, int color, float drag, int lifetime) {
		super(position, velocity, color);
		this.drag = drag;
		this.lifetime = lifetime;
	}

	@Override
	public void onUpdate() {
		PVector currentVelocity = getVelocity();
		//int currentColor = super.getColor();
		//int newColor = v.lerpColor(currentColor, 0, (float)(initTime + System.currentTimeMillis()) / (initTime + lifetime));

		addVelocity(currentVelocity.setMag(-drag * .1F));
		
		lifetime--;
	}

	/**
	 * Get the lifetime of the particle
	 *
	 * @return particle lifetime
	 */
	public boolean dead() {
		return lifetime <= 0;
	}

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
		return RenderLevel.AROUND_PARTICLE;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}
}
