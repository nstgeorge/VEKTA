package vekta.object.planet;

import processing.core.PVector;
import vekta.Counter;
import vekta.WorldGenerator;
import vekta.terrain.Terrain;

public class Moon extends TerrestrialPlanet {

	private final Planet parent;
	private final float orbitDistance;

	private final Counter orbitCt = new Counter(10).randomize();

//	private final PVector lastParentPosition = new PVector();

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
	public void onUpdate() {
		if(orbitCt.cycle() && !getParent().isDestroyed()) {
			// Ensure moon stays in orbit
			PVector offset = getPosition().sub(getParent().getPosition());
			offset.setMag(getOrbitDistance());
			getPositionReference().set(getParent().getPosition().add(offset));
			WorldGenerator.orbit(getParent(), this, 0);
		}
	}

	@Override
	public int getTrailLength() {
		return 1;
	}

//	@Override
//	public void draw(RenderLevel level, float r) {
//		if(RenderLevel.AROUND_PLANET.isVisibleTo(level)) {
//			super.draw(level, r);
//		}
//	}
//
//	@Override
//	public void updateTrail() {
//		// Sets trail relative to parent. TODO: generalize
//		super.updateTrail();
//
//		PVector parentPos = getParent().getPosition();
////		PVector offset = parentPos.copy().sub(lastParentPosition);
//		for(int i = 0; i < trail.length; i++) {
//			PVector pos = trail[i];
//			if(pos != null) {
//				pos.sub(getParent().getVelocity().div(getWorld().getTimeScale()));
//			}
//		}
////		lastParentPosition.set(parentPos);
//	}

//	@Override
//	public void updateOrigin(PVector offset) {
//		super.updateOrigin(offset);
//		lastParentPosition.add(offset);
//	}
}
