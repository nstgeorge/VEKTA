package vekta.object.ship;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.module.*;
import vekta.particle.ParticleEmitter;

import static processing.core.PConstants.PI;
import static processing.core.PConstants.QUARTER_PI;
import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

public class PlayerShip extends ModularShip {
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 4; // Base turn speed (RCS turnSpeed = 1)

	private ParticleEmitter emitter;

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		// Default modules
		addModule(new EngineModule(1));
		addModule(new RCSModule(1));
		addModule(new TargetingModule());
		addModule(new BatteryModule(100));
		addModule(new RadiatorModule(1));
		addModule(new CannonModule());

		setEnergy(getMaxEnergy());

		emitter = new ParticleEmitter(QUARTER_PI, 10, 1, UI_COLOR, v.color(255, 0, 0));
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
	public void draw(RenderLevel level, float r) {
		super.draw(level, r);

		if(getRenderLevel().isVisibleTo(level)) {
			// Draw engine emission
			if(getThrustControl() > 0) {
				float addition = v.random(1, 2);
				v.line(-r / 2, r * 2, 0, r * (2 + addition));
				v.line(r / 2, r * 2, 0, r * (2 + addition));
			}
		}

		// Particle testing
		if(getRenderLevel().isVisibleTo(level)) {
			emitter.update(getPosition(), getVelocity().rotate(PI).normalize());
		}
		
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
