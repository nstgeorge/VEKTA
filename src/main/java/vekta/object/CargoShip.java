package vekta.object;

import processing.core.PVector;
import vekta.Counter;

public class CargoShip extends Ship {
	// CargoShip defaults
	private static final float DEF_MASS = 25000;
	private static final float DEF_RADIUS = 15;
	private static final float DEF_SPEED = .01F;
	private static final float DEF_TURN = 1;

	private float thrust;
	private float turn;

	private final Counter steerCt = new Counter();

	public CargoShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, DEF_MASS, DEF_RADIUS, heading, position, velocity, color, DEF_SPEED, DEF_TURN);
	}

	@Override 
	public boolean isLanding() {
		return true;
	}
	
	@Override
	public float getThrustControl() {
		return thrust;
	}

	@Override
	public float getTurnControl() {
		return turn;
	}

	@Override
	public void onUpdate() {
		if(steerCt.cycle()) {
			steerCt.delay((int)v.random(10, 500));
			thrust = v.random(-1, 1);
			turn = v.random(-1, 1);
		}
		accelerate(thrust);
		turn(turn);
	}

	@Override
	public void draw() {
		drawShip(ShipModelType.CARGO_SHIP);
	}
}  
