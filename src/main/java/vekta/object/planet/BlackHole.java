package vekta.object.planet;

import processing.core.PVector;
import vekta.terrain.Terrain;
import vekta.world.RenderLevel;
import vekta.terrain.BlackHoleTerrain;

import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class BlackHole extends TerrestrialPlanet {
	private static final float EVENT_HORIZON_DENSITY = 1.85e19F;
	private static final float INTERACT_DENSITY = 10;
	private static final float PREVIEW_PULSE_SPEED = .03F;

	public BlackHole(String name, float mass, PVector position, PVector velocity, int color) {
		super(name, mass, INTERACT_DENSITY, position, velocity, color);
	}

	@Override
	protected Terrain chooseTerrain() {
		return new BlackHoleTerrain(this);
	}

	@Override
	protected float chooseAtmosphereDensity() {
		return 0;
	}

	//	@Override
//	protected Terrain createTerrain() {
//		return new BlackHoleTerrain(this);
//	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.INTERSTELLAR;
	}

	@Override
	public float getValueScale() {
		return 10;
	}

	@Override
	public float getMarkerScale() {
		return super.getMarkerScale() * 2;
	}

	@Override
	public void draw(RenderLevel level, float r) {
		drawMarker();
	}

	@Override
	public void drawPreview(float r) {
		float phase = sq(getRadius()) % 1e5F;
		for(float f = 0; f < 1; f += .05F) {
			float pulse = sin(v.frameCount * PREVIEW_PULSE_SPEED * f + phase);
			float s = r * (sq(f) - pulse * .1F);
			v.stroke(v.lerpColor(0, getColor(), (f * pulse + 1) / 2));
			v.ellipse(0, 0, s, s);
		}
	}
}
