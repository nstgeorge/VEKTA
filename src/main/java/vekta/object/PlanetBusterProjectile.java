package vekta.object;

import processing.core.PVector;
import vekta.Resources;
import vekta.object.particle.*;
import vekta.object.planet.Planet;
import vekta.world.RenderLevel;

import static processing.core.PApplet.sq;
import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class PlanetBusterProjectile extends HomingProjectile {
	private static final int EXPLOSION_PARTICLES = 150;
	private static final int EXPLOSION_SHOCKWAVES = 10;
	private static final float EXPLOSION_SCALE = .00015F;

	public PlanetBusterProjectile(SpaceObject parent, SpaceObject target, PVector position, PVector velocity, int color) {
		super(parent, target, 1000, position, velocity, color);
	}

	@Override
	public float getRadius() {
		return super.getRadius() * 2;
	}

	@Override
	public float getDespawnTime() {
		return 1000; // Don't despawn anytime soon
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
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return s instanceof Planet && super.collidesWith(level, s);
	}

	@Override
	public void onDestroyed(SpaceObject s) {
		if(s instanceof Planet) {
			PVector position = s.getPosition();
			PVector velocity = s.getVelocity();

			Resources.stopAllSounds();
			Resources.playSound("planetExplosionStart");

			ColorSelector colorRange = new ColorRange(s.getColor(), 255);
			ParticleStyle style = new ParticleStyle()
					.startColor(colorRange)
					.endColor(new ConstantColor(0))
					.lifetime(10);

			PVector particleVelocity = velocity.add(s.relativeVelocity(this).mult(.1F));
			for(int i = 0; i < EXPLOSION_PARTICLES; i++) {
				register(new Particle(
						getParentObject(),
						position,
						PVector.random2D().mult(v.random(s.getRadius() * EXPLOSION_SCALE)).add(particleVelocity),
						style));
			}

			for(int i = 0; i < EXPLOSION_SHOCKWAVES; i++) {
				int color = v.chance(.5F) ? 255 : colorRange.selectColor();
				register(new Shockwave(s, v.random(.2F, 2), (int)(sq(v.random(.2F, 1)) * 250), color));
			}

			register(new SoundShockwave("planetExplosion", 3, s, 10, 500, 100));

			s.destroyBecause(getParentObject());
		}

		super.onDestroyed(s);
	}
}

