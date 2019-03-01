package vekta.object;

import processing.core.PVector;
import vekta.Resources;
import vekta.Vekta;

import java.util.List;

import static vekta.Vekta.*;

public class PlayerShip extends Ship implements Targeter {
	// Default PlayerShip stuff
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float LANDING_SPEED = 1F;
	private static final float PROJECTILE_SPEED = 7;

	// Exclusive PlayerShip things
	private final int controlScheme; // Defined by CONTROL_DEF: 0 = WASD, 1 = IJKL
	private int ammo;
	private float thrust;
	private float turn;

	// Landing doodads
	private boolean landing;
	private final PVector influence = new PVector();

	private SpaceObject target;

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color, int ctrl, float speed, int turnSpeed, int ammo) {
		super(name, DEF_MASS, DEF_RADIUS, heading, position, velocity, color, speed, turnSpeed);
		this.controlScheme = ctrl;
		this.ammo = ammo;
	}

	@Override
	public SpaceObject getTarget() {
		return target;
	}

	@Override
	public void setTarget(SpaceObject target) {
		this.target = target;
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		return obj instanceof Planet;
	}

	@Override public boolean shouldResetTarget() {
		return true;
	}

	@Override
	public void draw() {
		super.draw();
		Vekta v = getInstance();

		// Draw influence vector
		v.stroke(255, 0, 0);
		v.line(position.x, position.y, position.x + (influence.x * 100), position.y + (influence.y * 100));
	}

	@Override
	public void onUpdate() {
		accelerate(thrust);
		turn(turn);

		if(landing && getTarget() != null) {
			PVector relative = velocity.copy().sub(getTarget().getVelocity());
			float mag = relative.mag();
			if(mag > 0) {
				heading.set(relative).normalize();
				float approachFactor = Math.min(1, 5 * getTarget().getRadius() / getTarget().getPosition().sub(position).mag());
				thrust = Math.max(-1, Math.min(1, (LANDING_SPEED * (1 - approachFactor / 2) - mag) * approachFactor / getSpeed()));
			}
		}
	}

	public void keyPress(char key) {
		landing = false;
		if(controlScheme == 0) {   // WASD
			switch(key) {
			case 'w':
				Resources.loopSound("engine");
				thrust = 1;
				break;
			case 'a':
				turn = -1;
				break;
			case 's':
				Resources.loopSound("engine");
				thrust = -1;
				break;
			case 'd':
				turn = 1;
				break;
			case 'x':
				fireProjectile();
				break;
			case 'z':
				fireProjectile();
				break;
			case '\t':
				landing = true;
				break;
			}
		}
		// TODO: map these keys using a config object rather than as switch statements
		if(controlScheme == 1) {   // IJKL
			switch(key) {
			case 'i':
				Resources.stopSound("engine");
				thrust = 1;
				break;
			case 'j':
				turn = -1;
				break;
			case 'k':
				Resources.stopSound("engine");
				thrust = -1;
				break;
			case 'l':
				turn = 1;
				break;
			case 'm':
				fireProjectile();
				break;
			case ',':
				fireProjectile();
				break;
			case '\\':
				landing = true;
				break;
			}
		}
	}

	public void keyReleased(char key) {
		if((key == 'w' || key == 's') && controlScheme == 0) {
			Resources.stopSound("engine");
			thrust = 0;
		}
		if((key == 'a' || key == 'd') && controlScheme == 0) {
			turn = 0;
		}

		if((key == 'i' || key == 'k') && controlScheme == 1) {
			Resources.stopSound("engine");
			thrust = 0;
		}
		if((key == 'j' || key == 'l') && controlScheme == 1) {
			turn = 0;
		}
	}

	private void fireProjectile() {
		if(ammo > 0) {
			Resources.playSound("laser");
			addObject(new Projectile(this, position.copy(), heading.copy().setMag(PROJECTILE_SPEED).add(velocity), getColor()));
			ammo--;
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		getWorld().setDead();
	}

	public boolean isLanding() {
		return landing;
	}

	public int getAmmo() {
		return ammo;
	}

	@Override
	public PVector applyInfluenceVector(List<SpaceObject> objects) {
		this.influence.set(super.applyInfluenceVector(objects));
		return this.influence;
	}
}  
