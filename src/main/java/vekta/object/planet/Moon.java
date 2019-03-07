package vekta.object.planet;

import processing.core.PVector;
import vekta.Counter;
import vekta.RenderLevel;
import vekta.WorldGenerator;
import vekta.terrain.Terrain;

import static vekta.Vekta.getWorld;

public class Moon extends TerrestrialPlanet {

	private final Planet parent;
	private final float orbitDistance;

	private final Counter orbitCt = new Counter(10).randomize();

	private final PVector lastParentPosition = new PVector();
	private float lastTimeScale = 1;

	public Moon(Planet parent, String name, float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(name, mass, density, terrain, position, velocity, color);

		this.parent = parent;
		this.orbitDistance = parent.getPosition().sub(position).mag();
	}

	public final Planet getParent() {
		return parent;
	}

	public float getOrbitDistance() {
		return orbitDistance;
	}

	@Override
	public boolean impartsGravity() {
		return false;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		if(orbitCt.cycle() && !getParent().isDestroyed()) {
			// Ensure moon stays in orbit
			PVector offset = getPosition().sub(getParent().getPosition());
			offset.setMag(getOrbitDistance());
			getPositionReference().set(getParent().getPosition().add(offset));
			WorldGenerator.orbit(getParent(), this, 0);
		}
	}

	@Override
	public void draw(RenderLevel level, float r) {
		if(RenderLevel.AROUND_PLANET.isVisibleTo(level)) {
			super.draw(level, r);
		}
	}
	
	@Override
	public void updateTrail() {
		super.updateTrail();

		float timeScale = getWorld().getTimeScale();
		PVector parentPos = getParent().getPosition();
		PVector offset = parentPos.copy().sub(lastParentPosition).mult(timeScale / lastTimeScale);
		for(PVector pos : trail) {
			if(pos != null) {
				pos.add(offset);
			}
		}
		lastParentPosition.set(parentPos);
		lastTimeScale = timeScale;
	}

	@Override
	public void updateOrigin(PVector offset) {
		super.updateOrigin(offset);
		lastParentPosition.add(offset);
	}
}
