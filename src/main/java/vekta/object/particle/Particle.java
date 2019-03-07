package vekta.object.particle;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.SpaceObject;

import static vekta.Vekta.removeObject;
import static vekta.Vekta.v;

public class Particle extends SpaceObject {
	private final ParticleStyle style;

	private final int endColor;
	private float lifetime;

	public Particle(PVector position, PVector velocity, ParticleStyle style) {
		super(position, velocity, style.getStartColor().selectColor());

		this.style = style;

		this.endColor = style.getEndColor().selectColor();
	}

	public final ParticleStyle getStyle() {
		return style;
	}

	@Override
	public int getColor() {
		return v.lerpColor(super.getColor(), endColor, lifetime / getStyle().getLifetime());
	}

	@Override
	public void onUpdate() {
		PVector currentVelocity = getVelocity();

		addVelocity(currentVelocity.setMag(-getStyle().getDrag()));

		lifetime += 1 / v.frameRate;
		if(lifetime >= getStyle().getLifetime()) {
			removeObject(this);
		}
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
		return RenderLevel.AROUND_SHIP;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}
}
