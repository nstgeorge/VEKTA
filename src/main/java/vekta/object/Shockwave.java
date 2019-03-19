package vekta.object;

import vekta.RenderLevel;

import static vekta.Vekta.*;

public class Shockwave extends SpaceObject {
	private float radius;
	private float speed;
	private final int time;

	private int aliveTime;

	public Shockwave(SpaceObject relative, float speed, int time, int color) {
		super(relative.getPosition(), relative.getVelocity(), color);

		this.radius = relative.getRadius();
		this.speed = speed;
		this.time = time;
	}

	@Override
	public String getName() {
		return "Shockwave";
	}

	@Override
	public float getMass() {
		return 1;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public int getColor() {
		return v.lerpColor(0, super.getColor(), sq(1 - (float)aliveTime / time));
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.STAR;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return false;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		if(++aliveTime > time) {
			despawn();
			return;
		}

		radius += speed * pow(radius, 1 / 3F) * getWorld().getTimeScale();
	}

	@Override
	public void draw(RenderLevel level, float r) {
		v.noFill();///
		for(float f = 1; f > 0; f -= .4F) {
			v.ellipse(0, 0, r * f, r * f);
		}
	}
}
