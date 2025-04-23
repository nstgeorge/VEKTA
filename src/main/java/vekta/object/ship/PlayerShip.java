package vekta.object.ship;

import processing.core.PVector;
import vekta.module.*;
import vekta.world.RenderLevel;

import static vekta.Vekta.v;

public class PlayerShip extends ModularShip {
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 4; // Base turn speed (RCS turnSpeed = 1)

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		// Default modules
		addModule(new EngineModule(1));
		addModule(new RCSModule(1));
		addModule(new TargetingModule());
		addModule(new BatteryModule(new Battery(100)));
		addModule(new PassiveTCSModule(1));
		addModule(new CannonModule());

		setEnergy(getMaxEnergy());
	}

	@Override
	public float getRadius() {
		return DEF_RADIUS;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		super.onUpdate(level);

		//				// Test particle system
		//				if(getRenderLevel().isVisibleTo(level)) {
		//					emitter.setValue(getHeading().rotate(HALF_PI));
		//				}
	}

	@Override
	public void draw(RenderLevel level, float r) {
		super.draw(level, r);

		if(getRenderLevel().isVisibleTo(level)) {
			// Draw engine emission
			if(getThrustControl() > 0 && hasEnergy()) {
				float addition = v.random(1, 2);
				v.line(-r / 2, r * 2, 0, r * (2 + addition));
				v.line(r / 2, r * 2, 0, r * (2 + addition));
			}
		}
	}

	@Override
	public void drawNearby(float r) {
		// Estimate heading for multiplayer
		if(isRemote() && velocity.magSq() > 1) {
			setHeading(getVelocity());
		}
		drawReentryEffect(r);//////
		drawShip(r, ShipModelType.DEFAULT);
	}

	@Override
	public void drawDistant(float r) {
		drawNearby(getRadius());
		//		drawMarker();
	}
}
