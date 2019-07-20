package vekta.module;

import processing.core.PVector;
import vekta.InfoGroup;
import vekta.KeyBinding;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.*;

public class AutopilotModule extends TargetingModule {
	private static final float APPROACH_SCALE = 1F;
	private static final float SLOWDOWN_FACTOR = 1F;
	private static final float CORRECT_FACTOR = 4F;
	private static final float ROTATE_SMOOTH = .1F;

	public AutopilotModule() {
		super();
	}

	public boolean isActive() {
		return getShip().isLanding();
	}

	public void setActive(boolean active) {
		getShip().setLanding(active);
		getShip().updateTargets();

		// Only zoom in or out as relevant
		getWorld().setAutoZoomDirection(!active);
	}

	@Override
	public String getName() {
		return "Autopilot Computer";
	}

	@Override
	public int getMass() {
		return super.getMass() + 100;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof TargetingModule && !(other instanceof AutopilotModule);
	}

	@Override
	public Module createVariant() {
		return new AutopilotModule();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		ModularShip ship = getShip();
		SpaceObject target = getTarget();
		if(isActive() && target != null && ship.consumeEnergyOverTime(5 * PER_MINUTE)) {
			// Grab telemetry vectors
			PVector position = ship.getPosition();
			PVector velocity = ship.getVelocity();
			PVector heading = ship.getHeading();
			PVector offset = target.getPosition().sub(position);
			PVector relative = target.getVelocity().sub(velocity);

			// Compute desired velocity
			float dist = offset.mag();
			float dot = offset.copy().normalize().dot(relative);

			float stoppingSpeed = sqrt(2 * ship.getSpeed() * dist);
			float approachSpeed = min(
					stoppingSpeed * SLOWDOWN_FACTOR + dot,
					APPROACH_SCALE * (1 + target.getRadius() / dist));

			PVector correct = relative.sub(offset.copy().setMag(dot / dist)); // Correction vector

			PVector desiredVelocity = target.getVelocity()
					.add(correct.mult(CORRECT_FACTOR * sqrt(dist) * getWorld().getTimeScale())) // Cancel tangential velocity
					.add(offset.copy().mult(approachSpeed * getWorld().getTimeScale()));

			// Choose direction to fire engines
			float dir = heading.dot(velocity.copy().sub(desiredVelocity)) >= 0 ? 1 : -1;
			float deltaV = velocity.dist(desiredVelocity);

			// Set heading and engine power
			PVector newHeading = velocity.sub(desiredVelocity).setMag(dir).add(ship.getHeading());
			ship.setHeading(PVector.lerp(heading, newHeading, ROTATE_SMOOTH));
			ship.setThrustControl(Math.max(-1, Math.min(1, -dir * deltaV / ship.getSpeed())));
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		super.onKeyPress(key);

		if(key == KeyBinding.SHIP_LAND) {
			setActive(true);
		}
		else if(key == KeyBinding.SHIP_FORWARD || key == KeyBinding.SHIP_BACKWARD || key == KeyBinding.SHIP_LEFT || key == KeyBinding.SHIP_RIGHT) {
			setActive(false);
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		super.onInfo(info);

		info.addKey(KeyBinding.SHIP_LAND, "toggle autopilot");
	}
}
