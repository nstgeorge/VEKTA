package vekta.object.module;

import processing.core.PVector;
import vekta.object.ControllableShip;
import vekta.object.SpaceObject;

import static vekta.Vekta.getWorld;

public class AutopilotModule extends TargetingModule {
	private static final float APPROACH_SCALE = .02F;

	public AutopilotModule() {
		super();
	}

	public boolean isActive() {
		return getShip().isLanding();
	}

	public void setActive(boolean active) {
		getShip().setLanding(active);
		getWorld().updateTargeters(getShip());
	}

	@Override
	public String getName() {
		return "Autopilot Computer";
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof TargetingModule && !(other instanceof AutopilotModule);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		ControllableShip ship = getShip();
		SpaceObject target = getTarget();
		if(isActive() && target != null) {
			// Grab telemetry vectors
			PVector position = ship.getPosition();
			PVector velocity = ship.getVelocity();
			PVector offset = target.getPosition().sub(position);
			PVector relative = target.getVelocity().sub(velocity);

			// Compute desired velocity
			float correctAmount = target.getRadius() / offset.mag();
			float approachSpeed = APPROACH_SCALE * (1 + correctAmount);
			PVector desiredVelocity = offset.mult(approachSpeed).add(target.getVelocity());

			// Choose direction to fire engines
			float dir = offset.dot(velocity.copy().sub(desiredVelocity)) >= 0 ? -1 : 1;
			float deltaV = velocity.dist(desiredVelocity);

			// Set heading and engine power
			PVector newHeading = velocity.sub(desiredVelocity).setMag(-dir).add(ship.getHeading());
			ship.setHeading(PVector.lerp(ship.getHeading(), newHeading, .1F));
			ship.setThrustControl(Math.max(-1, Math.min(1, dir * deltaV / ship.getSpeed())));
		}
	}

	@Override
	public void onKeyPress(char key) {
		super.onKeyPress(key);

		if(key == '\t') {
			setActive(true);
		}
		else if(key == 'w' || key == 'a' || key == 's' || key == 'd')/* TODO: reference external key mapping */ {
			setActive(false);
		}
	}

	@Override
	public void onDepart(SpaceObject s) {
		setActive(false);
	}
}
