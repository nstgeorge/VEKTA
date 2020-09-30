package vekta.object.particle;

import processing.core.PVector;
import vekta.world.RenderLevel;
import vekta.object.SpaceObject;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class Particle extends SpaceObject {
	private final SpaceObject parent;
	private final ParticleStyle style;

	private final int endColor;

	public Particle(SpaceObject parent, PVector position, PVector velocity, ParticleStyle style) {
		super(position, velocity, style.getStartColor().selectColor());

		if(parent == null) {
			throw new RuntimeException("Particle parent cannot be null");
		}

		this.parent = parent;
		this.style = style;

		this.endColor = style.getEndColor().selectColor();

		/// TEMP
		applyVelocity(getVelocity().mult(-1));
	}

	public final ParticleStyle getStyle() {
		return style;
	}

	@Override
	public int getColor() {
		return v.lerpColor(super.getColor(), endColor, getAliveTime() / getStyle().getLifetime());
	}

	@Override
	public void onUpdate(RenderLevel level) {
		//		addVelocity(relativeVelocity(parent).mult(getStyle().getDrag()));

		if(getAliveTime() >= getStyle().getLifetime()) {
			despawn();
		}
	}

	@Override
	public String getName() {
		return "Particle";
	}

	@Override
	public float getMass() {
		return 1;
	}

	@Override
	public float getRadius() {
		return 1;
	}

	//	@Override
	//	public RenderLevel getRenderLevel() {
	//		return RenderLevel.SHIP;
	//	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.STAR;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	@Override
	public void updateTrail() {
		super.updateTrail();

		// Update relative to parent
		PVector offset = parent.getVelocity().mult(getWorld().getTimeScale());
		for(float[] pos : trail) {
			if(pos != null) {
				pos[0] += offset.x;
				pos[1] += offset.y;
			}
		}
	}

	@Override
	public int getTrailLength() {
		return super.getTrailLength() / 2;
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return false;
	}
}
