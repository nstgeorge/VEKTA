package vekta.object.ship;

import processing.core.PVector;
import vekta.module.*;

public class PlayerShip extends ModularShip {
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 4; // Base turn speed (RCS turnSpeed = 1)

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		// Default modules
		addModule(new EngineModule(1));
		addModule(new RCSModule(1));
		addModule(new TargetingModule());
		addModule(new BatteryModule(100));
		addModule(new CannonModule());

		setEnergy(getMaxEnergy());
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
	public void drawNearby(float r) {
		drawShip(r, ShipModelType.DEFAULT);
	}

	@Override
	public void drawDistant(float r) {
		drawNearby(getRadius());
//		drawMarker();
	}
}  
