package vekta.object.ship;

import processing.core.PVector;
import vekta.RenderDistance;
import vekta.module.*;
import vekta.object.SpaceObject;

import java.util.List;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class PlayerShip extends ModularShip {
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 20; // Base turn speed (RCS turnSpeed = 1)

	private final PVector influence = new PVector();

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
	public void draw(RenderDistance dist) {
		if(dist.isNearby()) {
			v.pushMatrix();
			drawShip(dist, ShipModelType.DEFAULT);
			v.popMatrix();

			// Draw influence vector
			v.stroke(255, 0, 0);
			v.line(0, 0, (influence.x * 100), (influence.y * 100));
		}
	}

	@Override
	public void onUpdate() {
		for(Module module : getModules()) {
			module.onUpdate();
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		getWorld().setDead();
	}

	@Override
	public PVector applyInfluenceVector(List<SpaceObject> objects) {
		this.influence.set(super.applyInfluenceVector(objects));
		return this.influence;
	}
}  
