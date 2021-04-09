package vekta.object.planet;

import processing.core.PVector;
import vekta.Renameable;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.world.RenderLevel;

import static vekta.Vekta.*;

/**
 * A planet-like object.
 */
public abstract class Planet extends SpaceObject implements Renameable {
	private static final float SURVEY_SCAN_SPEED = .01F;

	private static final float SPLIT_MASS_ABSORB = .5F;

	private String name;
	private final float density;
	private float mass;
	private float radius;

	public Planet(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(position, velocity, color);
		this.name = name;
		this.density = density;

		setMass(mass); // Configure mass and radius
	}

	public String getLabel() {
		return getName();
	}

	public int getLabelColor() {
		return getColor();
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

	protected boolean shouldDrawPoint(RenderLevel level, float r) {
		return r < .5F && level.isVisibleTo(RenderLevel.INTERSTELLAR);
	}

	@Override
	public void draw(RenderLevel level, float r) {
		if(shouldDrawPoint(level, r)) {
			v.point(0, 0);
		}
		else {
			v.strokeWeight(2);
			super.draw(level, r);
			v.strokeWeight(1);
		}
	}

	public void drawLabel(float r) {
		String label = getLabel();
		if(label != null) {
			v.fill(getLabelColor());
			// v.textMode(LEFT);
			// v.text(label, r * 1.1F + 10, 5);
		}
	}

	@Override
	public void drawNearby(float r) {
		v.ellipse(0, 0, r, r);

		drawLabel(r);
	}

	@Override
	public void drawDistant(float r) {
		super.drawDistant(r);

		drawNearby(r);// Draw both reticle and planet
	}

	@Override
	public void drawPreview(float r) {
		// Render parameters (will eventually derive from planet properties)
		float rotateSpeed = 1F / 2000;
		float resolution = 32;
		float perspective = 1;

		float rotate = v.frameCount * rotateSpeed;
		float scan = v.frameCount * SURVEY_SCAN_SPEED;
		int color = getColor();

		v.shapeMode(CENTER);
		v.strokeWeight(2);
		v.noFill();

		// Draw scanner arc
		float scanScale = cos(scan);
		v.stroke(v.lerpColor(0, color, sq(cos(scan / 2 + perspective))));
		v.arc(0, 0, r * scanScale, r, -HALF_PI, HALF_PI);

		// Draw planet
		for(float f = 0; f < TWO_PI; f += TWO_PI / resolution) {
			float angle = f + rotate;
			float xScale = cos(angle);
			v.stroke(v.lerpColor(0, color, sq(cos(f / 2 + perspective))));
			v.arc(0, 0, r * xScale, r, -HALF_PI, HALF_PI);
		}

		v.strokeWeight(1);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(getMass() * 2 >= s.getMass()) {
			if(s instanceof Moon && ((Moon)s).getOrbitObject() == this) { // TODO: move to `Moon` when collision checking is upgraded
				return;
			}
			s.destroyBecause(this);
		}
	}

	@Override
	public void onDestroyed(SpaceObject s) {
		// Split planet into pieces
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
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
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
