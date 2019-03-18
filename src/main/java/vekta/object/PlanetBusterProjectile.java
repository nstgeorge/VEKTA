package vekta.object;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.particle.*;
import vekta.object.planet.Planet;

import static processing.core.PApplet.sq;
import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class PlanetBusterProjectile extends HomingProjectile {
	private static final int EXPLOSION_PARTICLES = 100;
	private static final int EXPLOSION_SHOCKWAVES = 10;
	private static final float EXPLOSION_SCALE = .001F;

	public PlanetBusterProjectile(SpaceObject parent, SpaceObject target, PVector position, PVector velocity, int color) {
		super(parent, target, 1000, position, velocity, color);
	}

	@Override
	public float getRadius() {
		return super.getRadius() * 2;
	}

	@Override
	public int getDespawnTime() {
		return super.getDespawnTime() * 10; // Don't despawn anytime soon
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.STAR;
	}

	@Override
	public RenderLevel getDespawnLevel() {
		return getRenderLevel();
	}

	@Override
	public void onDestroy(SpaceObject s) {
		if(s instanceof Planet) {
			PVector position = s.getPosition();
			PVector velocity = s.getVelocity();

			ColorSelector colorRange = new ColorRange(s.getColor(), 255);

			ParticleStyle style = new ParticleStyle()
					.withStartColor(colorRange)
					.withEndColor(new ConstantColor(0))
					.withLifetime(10);

			for(int i = 0; i < EXPLOSION_PARTICLES; i++) {
				register(new Particle(s, position, PVector.random2D().mult(v.random(s.getRadius() * EXPLOSION_SCALE)).add(velocity), style));
			}

			for(int i = 0; i < EXPLOSION_SHOCKWAVES; i++) {
				int color = v.chance(.5F) ? 255 : colorRange.selectColor();
				register(new Shockwave(s, v.random(.1F, .3F), (int)(sq(v.random(.2F, 1)) * 250), color));
			}
						
			s.destroyBecause(getParentObject());
		}

		super.onDestroy(s);
	}
}

