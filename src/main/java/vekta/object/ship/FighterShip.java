package vekta.object.ship;

import processing.core.PVector;
import vekta.object.HomingProjectile;
import vekta.object.Projectile;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.EventGenerator;
import vekta.util.Counter;
import vekta.util.Debounce;
import vekta.world.RenderLevel;

import static vekta.Vekta.*;

public abstract class FighterShip extends Ship implements Targeter {
	private static final float DEF_MASS = 1000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F;
	private static final float DEF_TURN = 4;

	private static final float DEF_ENGAGE_DIST = 2000;
	private static final float DEF_INTERCEPT_DIST = DEF_ENGAGE_DIST * getDistanceUnit(RenderLevel.PLANET);

	private static final float PROJECTILE_SPEED = 20;
	private static final float MIN_ATTACK = 10;
	private static final float MAX_ATTACK = 200;
	private static final float ATTACK_SPREAD = 10;
	private static final float SPEED_DAMPEN = .01F;
	private static final float INTERCEPT_CHANCE = .1F;

	private SpaceObject target;
	private final Counter attackCt = new Counter();

	private final Debounce interceptDebounce = new Debounce(30).setReady();

	public FighterShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);
	}

	@Override
	public float getMass() {
		return DEF_MASS;
	}

	@Override
	public float getRadius() {
		return DEF_RADIUS;
	}

	@Override
	public SpaceObject getSpaceObject() {
		return this;
	}

	public float getEngageDistance() {
		return DEF_ENGAGE_DIST;
	}

	public float getInterceptDistance() {
		return DEF_INTERCEPT_DIST;
	}

	@Override
	public SpaceObject getTarget() {
		return target;
	}

	@Override
	public void setTarget(SpaceObject obj) {
		this.target = obj;
	}

	@Override
	public boolean shouldUpdateTarget() {
		return true;
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		return target != null ? obj == target : obj instanceof ModularShip && !obj.isDestroyed() && ((ModularShip)obj).hasController();
	}

	@Override
	public void updateTargets() {
		getWorld().updateTargeter(this);
	}

	@Override
	public void onUpdateShip() {
		if(target != null) {
			PVector pos = target.getPosition().add(relativeVelocity(target).div(PROJECTILE_SPEED));
			PVector offset = pos.sub(position);

			float offsetSq = offset.magSq();
			float rangeFactor = sq(getEngageDistance()) - offsetSq;

			heading.set(offset.normalize());

			accelerate(max(-.5F, min(1F, -rangeFactor)));
			if(target.getColor() != getColor() && attackCt.cycle()) {
				attackCt.delay(chooseAttackTime());

				if(RenderLevel.SHIP.isVisibleTo(getWorld().getRenderLevel())) {
					interceptDebounce.reset();
					if(rangeFactor >= 0) {
						fireProjectile();
					}
				}

				if(target instanceof ModularShip && interceptDebounce.isReady() && v.chance(INTERCEPT_CHANCE)) {
					interceptDebounce.reset();

					ModularShip ship = (ModularShip)target;
					if(ship.hasController() && offsetSq <= sq(getInterceptDistance())) {
						getPositionReference().set(PVector.random2D()
								.normalize()
								.mult(v.random(.1F, .2F) * getEngageDistance())
								.add(target.getPositionReference()));

						float zoom = getDistanceUnit(RenderLevel.SHIP);
						if(getWorld().getZoom() > zoom) {
							getWorld().setZoom(zoom);
						}
						setVelocity(target.getVelocityReference());
						//						applyVelocity(getVelocity());////

						ship.getController().send("Intercepted!").withColor(getColor());
						EventGenerator.updateSituations(ship.getController());
					}
				}
			}

			addVelocity(relativeVelocity(target).mult(SPEED_DAMPEN));
		}
	}

	@Override
	public void drawNearby(float r) {
		drawShip(r, ShipModelType.FIGHTER);
	}

	@Override
	public boolean isDockable(SpaceObject s) {
		return /*super.isDockable(s) && */s instanceof TerrestrialPlanet;
	}

	public float getAttackScale() {
		return 1;
	}

	public int chooseAttackTime() {
		return (int)v.random(MIN_ATTACK, MAX_ATTACK);
	}

	public void fireProjectile() {
		getWorld().playSound("laser", position);
		PVector vel = getHeading().setMag(PROJECTILE_SPEED)
				.rotate(v.random(-ATTACK_SPREAD, ATTACK_SPREAD) * PI / 360)
				.mult(v.random(.9F, 1.1F))
				.add(velocity);

		register(v.chance(.2F)
				? new HomingProjectile(this, getTarget(), getAttackScale(), getPosition(), getVelocity(), getColor())
				: new Projectile(this, getPosition(), vel, getColor()));
	}
}  
