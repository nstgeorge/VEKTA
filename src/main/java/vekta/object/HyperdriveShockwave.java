package vekta.object;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.ship.Ship;

import static vekta.Vekta.v;

public class HyperdriveShockwave extends Shockwave {
	private final Ship relative;

	public HyperdriveShockwave(Ship relative, float speed, int time, int color) {
		super(relative, speed, time, color);

		this.relative = relative;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		super.onUpdate(level);

		setVelocity(relative.velocity);

		applyVelocity(relative.getHeading().mult(-relative.velocity.mag() * .3F)
				.add(PVector.random2D().mult(v.random(1e5F))));
	}

	@Override
	public void draw(RenderLevel level, float r) {
		v.ellipse(0, 0, r, r);
	}
}
