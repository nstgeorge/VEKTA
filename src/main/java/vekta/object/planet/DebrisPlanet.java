package vekta.object.planet;

import processing.core.PVector;
import vekta.world.RenderLevel;
import vekta.object.SpaceObject;

import static vekta.Vekta.getWorld;

/**
 * Model for a planet debris cloud after destruction.
 */
public class DebrisPlanet extends Planet {
	private static final float DECAY_FACTOR = 10;

	private final int splitsRemaining;

	private float decay = 0;

	public DebrisPlanet(String name, float mass, float density, int splitsRemaining, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		this.splitsRemaining = splitsRemaining;
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public boolean impartsGravity() {
		return false; // For now
	}

	@Override
	public int getSplitsRemaining() {
		return splitsRemaining;
	}

	@Override
	public int getTrailLength() {
		return 5;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		super.onUpdate(level);

		decay += DECAY_FACTOR * getWorld().getTimeScale();

		setMass(getMass() - decay);

		if(getMass() <= 0) {
			despawn();
		}
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return !(s instanceof DebrisPlanet) && super.collidesWith(level, s);
	}
}
