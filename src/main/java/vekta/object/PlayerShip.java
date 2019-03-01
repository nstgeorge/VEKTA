package vekta.object;

import processing.core.PVector;
import vekta.Resources;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.menu.Menu;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.UpgradeMenuOption;
import vekta.object.module.*;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.*;

public class PlayerShip extends Ship implements Targeter, Upgradeable {
	// Default PlayerShip stuff
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 20; // Base turn speed (RCS turnSpeed = 1)
	private static final float APPROACH_SPEED = 1F;
	private static final float PROJECTILE_SPEED = 7;

	// Exclusive PlayerShip things
	private final int controlScheme; // Defined by CONTROL_DEF: 0 = WASD, 1 = IJKL
	private int ammo;
	private float thrust;
	private float turn;

	// Landing doodads
	private boolean landing;
	private final PVector influence = new PVector();

	// Upgradeable modules
	private final List<Module> modules = new ArrayList<>();

	private SpaceObject target;

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color, int ctrl, int ammo) {
		super(name, DEF_MASS, DEF_RADIUS, heading, position, velocity, color, DEF_SPEED, DEF_TURN);
		this.controlScheme = ctrl;
		this.ammo = ammo;

		// Default modules
		addModule(new EngineModule(1));
		addModule(new RCSModule(1));
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

	@Override
	public boolean shouldResetTarget() {
		return true;
	}

	@Override
	public List<Module> getModules() {
		return modules;
	}

	@Override
	public Module getBestModule(ModuleType type) {
		Module module = null;
		for(Module m : getModules()) {
			if(m.getType() == type && m.isBetter(module)) {
				module = m;
			}
		}
		return module;
	}

	@Override
	public List<Module> findUpgrades() {
		List<Module> list = new ArrayList<>();
		for(Item item : getInventory()) {
			if(item instanceof ModuleItem) {
				list.add(((ModuleItem)item).getModule());
			}
		}
		return list;
	}

	@Override
	public void upgrade(Module module) {
		addModule(module);
		for(Item item : new ArrayList<>(getInventory().getItems())) {
			if(item instanceof ModuleItem && ((ModuleItem)item).getModule() == module) {
				getInventory().remove(item);
				break;
			}
		}
	}

	public void addModule(Module module) {
		// TODO: more control over module exclusivity
		for(Module m : new ArrayList<>(modules)) {
			if(m.getType() == module.getType()) {
				removeModule(m);
			}
		}
		modules.add(module);
	}

	public void removeModule(Module module) {
		if(modules.remove(module)) {
			getInventory().add(new ModuleItem(module));
		}
	}

	@Override
	public void draw() {
		drawShip(SHIP_SHAPE.DEFAULT);

		// Draw influence vector
		v.stroke(255, 0, 0);
		v.line(position.x, position.y, position.x + (influence.x * 100), position.y + (influence.y * 100));
	}

	@Override
	public void onUpdate() {
		for(Module module : getModules()) {
			module.update(this);
		}

		if(landing && getTarget() != null) {
			PVector relative = velocity.copy().sub(getTarget().getVelocity());
			float mag = relative.mag();
			if(mag > 0) {
				heading.set(relative).normalize();
				float approachFactor = Math.min(1, 5 * getTarget().getRadius() / getTarget().getPosition().sub(position).mag());
				thrust = Math.max(-1, Math.min(1, (APPROACH_SPEED * (1 - approachFactor / 2) - mag) * approachFactor / getSpeed()));
			}
		}
	}

	public void keyPress(char key) {
		for(Module module : getModules()) {
			module.keyPress(this, key);
		}
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
			case 'e':
				openMenu();
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

	public void openMenu() {
		Menu menu = new Menu(new ObjectMenuHandle(new BackOption(getWorld()), this));
		menu.add(new UpgradeMenuOption(this));
		menu.addDefault();
		setContext(menu);
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
