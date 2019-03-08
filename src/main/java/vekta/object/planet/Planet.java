package vekta.object.planet;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.terrain.MoltenTerrain;
import vekta.terrain.Terrain;

import static vekta.Vekta.*;

/**
 * Model for a planet.
 */
public abstract class Planet extends SpaceObject {
	private static final float MIN_SPLIT_RADIUS = 20 * SCALE; // TODO: improve cutoff
	private static final float SPLIT_OFFSET_SCALE = .25F;
	private static final float SPLIT_VELOCITY_SCALE = 1;
	private static final float SPLIT_MASS_ABSORB = .5F;

	private final String name;
	private float mass;
	private final float density;

	private float radiusCache;

	public Planet(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(position, velocity, color);
		this.name = name;
		this.mass = mass;
		this.density = density;

		updateRadius();
	}

	public boolean impartsGravity() {
		return getMass() >= MIN_GRAVITY_MASS;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.STAR;
	}

	@Override
	public float getSpecificHeat() {
		return 1; // TODO depend on planet properties
	}

	@Override
	public void draw(RenderLevel level, float r) {
		// Temp: fade out planet if zoomed in too much to render
		float maxRadius = 1000;
		if(r > maxRadius) {
			return;
		}
		if(r > maxRadius * .9F) {
			v.fill(v.lerpColor(0, getColor(), (maxRadius - r) / maxRadius));
		}
		v.strokeWeight(2);
		super.draw(level, r);
		v.strokeWeight(1);
	}

	@Override
	public void drawNearby(float r) {
		v.ellipse(0, 0, r, r);
	}

	@Override
	public void drawDistant(float r) {
		super.drawDistant(r);
		drawNearby(r);// Draw both reticle and planet
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return getColor() != s.getColor() && super.collidesWith(level, s);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(getMass() >= s.getMass() * 2) {
			if(s instanceof Moon && ((Moon)s).getParent() == this) {
				return;
			}
			s.destroyBecause(this);
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		//println("Planet destroyed with radius: " + getDistanceScale());

		// If sufficiently large, split planet in half
		if(getRadius() >= MIN_SPLIT_RADIUS) {
			float newMass = getMass() / 2;

			// Use mass-weighted collision velocity for base debris velocity
			float xWeight = getVelocity().x * getMass() + s.getVelocity().x * s.getMass();
			float yWeight = getVelocity().y * getMass() + s.getVelocity().y * s.getMass();
			float massSum = getMass() + s.getMass();
			PVector newVelocity = new PVector(xWeight / massSum, yWeight / massSum);

			PVector base = getPosition().copy().sub(s.getPosition()).normalize().rotate(PI / 2);
			PVector offset = base.copy().mult(getRadius() * SPLIT_OFFSET_SCALE);
			PVector splitVelocity = base.copy().mult(SPLIT_VELOCITY_SCALE);
			Terrain terrain = new MoltenTerrain();
			Planet a = new TerrestrialPlanet(Resources.generateString("planet_debris"), newMass, getDensity(), terrain, getPosition().copy().add(offset), newVelocity.copy().add(splitVelocity), getColor());
			Planet b = new TerrestrialPlanet(Resources.generateString("planet_debris"), newMass, getDensity(), terrain, getPosition().copy().sub(offset), newVelocity.copy().sub(splitVelocity), getColor());
			if(!s.collidesWith(getRenderLevel(), a)) {
				mass -= a.mass;
				addObject(a);
			}
			if(!s.collidesWith(getRenderLevel(), b)) {
				mass -= b.mass;
				addObject(b);
			}
		}

		// If this is a planetary collision, add some additional mass to the other planet
		if(mass > 0 && s instanceof Planet) {
			Planet p = (Planet)s;
			p.setMass(p.getMass() + mass * SPLIT_MASS_ABSORB);
		}
	}

	@Override
	public float getMass() {
		return mass;
	}

	@Override
	public String getName() {
		return name;
	}

	void setMass(float mass) {
		this.mass = mass;
		updateRadius();
	}

	@Override
	public float getRadius() {
		return radiusCache;
	}

	private void updateRadius() {
		radiusCache = pow(getMass() / getDensity(), (float)1 / 3)/* / SCALE*/;
	}

	public float getDensity() {
		return density;
	}
}
