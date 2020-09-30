package vekta.object;

import vekta.Resources;
import vekta.object.ship.ModularShip;
import vekta.world.RenderLevel;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class SoundShockwave extends Shockwave {
	private final String sound;
	private final float cameraImpact;

	private boolean played;

	public SoundShockwave(String sound, float cameraImpact, SpaceObject relative, float speed, int time, int color) {
		super(relative, speed, time, color);

		this.sound = sound;
		this.cameraImpact = cameraImpact;
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return s instanceof ModularShip && ((ModularShip)s).hasController();
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(!played) {
			played = true;
			Resources.playSound(sound);
			getWorld().addCameraImpact(cameraImpact);
		}
	}

	@Override
	public void draw(RenderLevel level, float r) {
		v.ellipse(0, 0, r, r);
	}
}
