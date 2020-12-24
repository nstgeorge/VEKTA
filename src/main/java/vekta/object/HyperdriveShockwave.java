package vekta.object;

import vekta.object.ship.Ship;
import vekta.world.RenderLevel;

import static processing.core.PApplet.sq;
import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class HyperdriveShockwave extends Shockwave {
	private final Ship relative;

	public HyperdriveShockwave(Ship relative, float speed, int time, int color) {
		super(relative, speed, time, color);

		this.relative = relative;
	}

//	@Override
//	public boolean isCurvable() {
//		return false;
//	}

	@Override
	public void onUpdate(RenderLevel level) {
		super.onUpdate(level);

		position.set(relative.getPosition().sub(relative.getHeading().mult(getRadius() + relative.getRadius() + sq(getRadius() * .2F) / getWorld().getZoom())));
	}

	@Override
	public void draw(RenderLevel level, float r) {
		v.ellipse(0, 0, r, r);
	}
}
