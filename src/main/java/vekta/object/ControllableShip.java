package vekta.object;

import processing.core.PVector;
import vekta.Resources;

import static vekta.Vekta.max;
import static vekta.Vekta.min;

public abstract class ControllableShip extends Ship {
	private boolean landing;
	private float thrust;
	private float turn;
	
	private float energy;
	private float maxEnergy;

	public ControllableShip(String name, float mass, float radius, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(name, mass, radius, heading, position, velocity, color, speed, turnSpeed);
	}

	public boolean isLanding() {
		return landing;
	}

	public void setLanding(boolean landing) {
		this.landing = landing;
	}

	public float getThrustControl() {
		return thrust;
	}

	public void setThrustControl(float thrust) {
		this.thrust = thrust;
		if(thrust != 0) {
			Resources.loopSound("engine");
		}
		else {
			Resources.stopSound("engine");
		}
	}

	public float getTurnControl() {
		return turn;
	}

	public void setTurnControl(float turn) {
		this.turn = turn;
	}

	public boolean hasEnergy() {
		return energy > 0;
	}

	public float getEnergy() {
		return energy;
	}

	public void setEnergy(float energy) {
		this.energy = max(0, min(getMaxEnergy(), energy));
	}

	public void addEnergy(float amount) {
		setEnergy(energy + amount);
	}

	public boolean consumeEnergy(float amount) {
		float output = energy - amount;
		setEnergy(output);
		return output >= 0;
	}

	public float getMaxEnergy() {
		return maxEnergy;
	}

	// TEMPORARY: only use by modules to adjust max energy
	public void addMaxEnergy(float amount) {
		maxEnergy += amount;
	}

	public void recharge() {
		setEnergy(getMaxEnergy());
	}
}  
