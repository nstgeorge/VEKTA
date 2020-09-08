package vekta.object.planet;

import processing.core.PVector;
import vekta.object.SpaceObject;
import vekta.util.Counter;
import vekta.world.RenderLevel;
import vekta.spawner.WorldGenerator;
import vekta.terrain.Terrain;

import static vekta.Vekta.STAR_LEVEL;
import static vekta.Vekta.getWorld;

public class Moon extends TerrestrialPlanet {

	//	private final Planet parent;
	private final float orbitDistance;

	private final Counter orbitCt = new Counter(20).randomize();

	//	private final PVector lastParentPosition = new PVector();
	//	private float lastTimeScale = 1;

	public Moon(Planet parent, String name, float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(name, mass, density, terrain, position, velocity, color);

		//		this.parent = parent;
		setOrbitObject(parent);
		this.orbitDistance = parent.getPosition().sub(position).mag();
	}

	@Override
	public String getLabel() {
		if(getWorld().getZoom() > STAR_LEVEL / 2) {
			return null;
		}
		String label = super.getLabel();
		if(label != null && getOrbitObject() != null) {
			return label.replace(getOrbitObject().getName(), "").trim();
		}
		return label;
	}

	@Override
	public float getValueScale() {
		return .75F;
	}

	//	public final Planet getParent() {
	//		return parent;
	//	}

	//	@Override
	//	public SpaceObject getOrbitObject() {
	//		return parent;
	//	}

	public float getOrbitDistance() {
		return orbitDistance;
	}

	@Override
	public boolean impartsGravity() {
		return false;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		SpaceObject parent = getOrbitObject();
		if(orbitCt.cycle() && !parent.isDestroyed()) {
			// Ensure moon stays in orbit
			PVector offset = getPosition().sub(parent.getPositionReference());
			offset.setMag(getOrbitDistance());
			//			getPositionReference().set(getParentObject().getPosition().add(offset));
			WorldGenerator.orbit(parent, this, 0);
			syncMovement(parent.getPosition().add(offset), getVelocity(), 0, orbitCt.getInterval());
		}
		super.onUpdate(level);
	}

	@Override
	protected void updateOrbitObject() {
	}

	//	@Override
	//	public void draw(RenderLevel level, float r) {
	//		if(RenderLevel.PLANET.isVisibleTo(level)) {
	//			super.draw(level, r);
	//		}
	//	}

	@Override
	public int getTrailLength() {
		return 1;
	}

	//	@Override
	//	public void updateTrail() {
	//		super.updateTrail();
	//
	//		float timeScale = getWorld().getTimeScale();
	//		PVector parentPos = getParentObject().getPosition();
	//		PVector offset = parentPos.copy().sub(lastParentPosition).mult(timeScale / lastTimeScale);
	//		for(PVector pos : trail) {
	//			if(pos != null) {
	//				pos.add(offset);
	//			}
	//		}
	//		lastParentPosition.set(parentPos);
	//		lastTimeScale = timeScale;
	//	}
	//
	//	@Override
	//	public void updateOrigin(PVector offset) {
	//		lastParentPosition.add(offset);
	//	}
}
