package vekta.object.particle;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.SpaceObject;

import static vekta.Vekta.*;

public class Particle extends SpaceObject {
	private final SpaceObject parent;
	private final ParticleStyle style;

	private final int endColor;
	private float aliveTime;

	public Particle(SpaceObject parent, PVector position, PVector velocity, ParticleStyle style) {
		super(position, velocity, style.getStartColor().selectColor());

		this.parent = parent;
		this.style = style;

		this.endColor = style.getEndColor().selectColor();
	}

	public SpaceObject getParent() {
		return parent;
	}

	public final ParticleStyle getStyle() {
		return style;
	}

	@Override
	public int getColor() {
		return v.lerpColor(super.getColor(), endColor, aliveTime / getStyle().getLifetime());
	}

	@Override
	public void onUpdate(RenderLevel level) {
		PVector currentVelocity = getVelocity();
		addVelocity(currentVelocity.setMag(-getStyle().getDrag()));

		aliveTime += 1 / v.frameRate;
		if(aliveTime >= getStyle().getLifetime()) {
			removeObject(this);
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

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.AROUND_SHIP;
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
		for(PVector pos : trail) {
			if(pos != null) {
				pos.add(offset);
			}
		}
	}
}
