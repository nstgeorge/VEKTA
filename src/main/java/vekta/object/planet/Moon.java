package vekta.object.planet;

import processing.core.PVector;
import vekta.Counter;
import vekta.RenderLevel;
import vekta.WorldGenerator;
import vekta.terrain.Terrain;

import static processing.core.PApplet.println;

public class Moon extends TerrestrialPlanet {

	private final Planet parent;
	private final float orbitDistance;

	private final Counter orbitCt = new Counter(10).randomize();

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
	public RenderLevel getRenderLevel() {
		return RenderLevel.AROUND_PLANET;
	}

	@Override
	public void onUpdate() {
		if(orbitCt.cycle() && !getParent().isDestroyed()) {
			// Ensure moon stays in orbit
			PVector offset = getPosition().sub(getParent().getPosition());
			offset.setMag(getOrbitDistance());
			getPositionReference().set(getParent().getPosition().add(offset));
			WorldGenerator.orbit(getParent(), this, 0);
			
			println(getPosition().sub(getParent().getPosition()).mag());
		}
	}
}
