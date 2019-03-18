package vekta.object.planet;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;

import static vekta.Vekta.*;

/**
 * Model for a planet.
 */
public abstract class Planet extends SpaceObject {
	private static final float MIN_SPLIT_RADIUS = 1e7F;
	private static final float SPLIT_MASS_ABSORB = .5F;

	private final String name;
	private final float density;

	private float mass;
	private float radius;

	public Planet(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(position, velocity, color);
		this.name = name;
		this.density = density;

		setMass(mass); // Configure mass and radius
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
	public int getTrailLength() {
		return super.getTrailLength() * 4;
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
		return super.collidesWith(level, s);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(getMass() * 2 >= s.getMass()) {
			if(s instanceof Moon && ((Moon)s).getParent() == this) { // TODO: move to Moon when collision checking is upgraded
				return;
			}
			s.destroyBecause(this);
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		//println("Planet destroyed with radius: " + getDistanceScale());

		// If sufficiently large, split planet into pieces
		if(getRadius() >= MIN_SPLIT_RADIUS) {
			// Use mass-weighted collision velocity for base debris velocity
			float xWeight = getVelocity().x * getMass() + s.getVelocity().x * s.getMass();
			float yWeight = getVelocity().y * getMass() + s.getVelocity().y * s.getMass();
			float massSum = getMass() + s.getMass();
			PVector newVelocity = new PVector(xWeight / massSum, yWeight / massSum);

			//			Terrain terrain = new MoltenTerrain();

			int splitsRemaining = getSplitsRemaining() - 1;
			if(splitsRemaining > 0) {
				int parts = (int)v.random(10, 20);
				float partMass = mass / parts;
				float partDensity = getDensity();
				for(int i = 0; i < parts; i++) {
					float massFactor = sq(v.random(.25F, 1));
					PVector offset = PVector.random2D().mult(v.random(getRadius() * .5F));
					PVector velocity = offset.mult(v.random(.05F, .1F) * .0001F / massFactor);

					register(new DebrisPlanet(
							Resources.generateString("planet_debris"),
							massFactor * partMass,
							v.random(.5F, 1) * partDensity,
							splitsRemaining,
							getPosition().add(offset),
							newVelocity.copy().add(velocity),
							v.lerpColor(getColor(), 0, v.random(.1F, .5F))));
				}
			}
		}

		// If this is a planetary collision, add some additional mass to the other planet
		if(mass > 0 && s instanceof Planet) {
			Planet p = (Planet)s;
			p.setMass(p.getMass() + mass * SPLIT_MASS_ABSORB);
		}
	}

	public int getSplitsRemaining() {
		return 3;
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
		this.radius = pow(getMass() / getDensity(), (float)1 / 3);
	}

	@Override
	public float getRadius() {
		return radius;
	}

	public float getDensity() {
		return density;
	}
}
