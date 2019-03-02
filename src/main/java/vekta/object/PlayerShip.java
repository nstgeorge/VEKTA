package vekta.object;

import processing.core.PVector;
import vekta.Resources;
import vekta.Vekta;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.*;
import vekta.object.module.*;
import vekta.terrain.LandingSite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static vekta.Vekta.*;

public class PlayerShip extends Ship implements Upgradeable {
	// Default PlayerShip stuff
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 20; // Base turn speed (RCS turnSpeed = 1)
	private static final float APPROACH_SPEED = .02F;

	// Exclusive PlayerShip things
	private final int controlScheme; // Defined by CONTROL_DEF: 0 = WASD, 1 = IJKL
	private float thrust;
	private float turn;
	private float energy;
	private float maxEnergy;

	private boolean autopilot;
	private final PVector influence = new PVector();

	// Upgradeable modules
	private final List<Module> modules = new ArrayList<>();

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color, int ctrl) {
		super(name, DEF_MASS, DEF_RADIUS, heading, position, velocity, color, DEF_SPEED, DEF_TURN);
		this.controlScheme = ctrl;

		// Default modules
		addModule(new EngineModule(1));
		addModule(new RCSModule(1));
		addModule(new TargetingModule());
		addModule(new BatteryModule(100));
		addModule(new CannonModule());

		setEnergy(getMaxEnergy() * .2F);
	}

	@Override
	public boolean isLanding() {
		return autopilot;
	}

	@Override
	public void onLand(LandingSite site) {
		Menu menu = new Menu(new LandingMenuHandle(site, getWorld()));
		site.getTerrain().setupLandingMenu(this, menu);
		menu.add(new TerrainInfoOption(site.getTerrain()));
		menu.addDefault();
		Resources.playSound("land");
		Vekta.setContext(menu);
	}

	@Override
	public void onDock(SpaceObject s) {
		if(s instanceof CargoShip) {
			Inventory inv = ((CargoShip)s).getInventory();
			Menu menu = new Menu(new ObjectMenuHandle(new ShipUndockOption(this, getWorld()), s));
			menu.add(new LootMenuOption("Loot", getInventory(), inv));
			menu.addDefault();
			setContext(menu);
		}
	}

	@Override
	public void onDepart(SpaceObject obj) {
		thrust = 0;
		turn = 0;
		autopilot = false;
	}

	@Override
	public float getThrustControl() {
		return thrust;
	}

	@Override
	public float getTurnControl() {
		return turn;
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

	@Override
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

	@Override
	public Collection<Targeter> getTargeters() {
		ArrayList<Targeter> list = new ArrayList<>(); // TODO cache
		for(Module m : getModules()) {
			if(m instanceof Targeter) {
				list.add((Targeter)m);
			}
		}
		return list;
	}

	public SpaceObject findTarget() {
		Targeter t = ((Targeter)getBestModule(ModuleType.TARGET_COMPUTER));
		return t != null ? t.getTarget() : null;
	}

	@Override
	public List<Module> getModules() {
		return modules;
	}

	@Override
	public Module getBestModule(ModuleType type) {
		Module module = null;
		for(Module m : getModules()) {
			if(m.getType() == type && (module == null || m.isBetter(module))) {
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
		module.onInstall(this);
	}

	public void removeModule(Module module) {
		if(modules.remove(module)) {
			getInventory().add(new ModuleItem(module));
		}
		module.onUninstall(this);
	}

	@Override
	public void draw() {
		drawShip(ShipModelType.DEFAULT);

		// Draw influence vector
		v.stroke(255, 0, 0);
		v.line(position.x, position.y, position.x + (influence.x * 100), position.y + (influence.y * 100));
	}

	@Override
	public void onUpdate() {
		for(Module module : getModules()) {
			module.onUpdate(this);
		}

		// TODO: externalize as TargeterModule upgrade
		SpaceObject target = findTarget();
		if(autopilot && target != null) {
			PVector offset = target.getPosition().sub(position);
			PVector relative = target.getVelocity().sub(velocity);
			PVector headingCorrection = relative.setMag(APPROACH_SPEED);
			PVector desiredVelocity = offset.mult(APPROACH_SPEED).add(target.getVelocity());
			float dir = offset.dot(getVelocity().sub(desiredVelocity)) >= 0 ? -1 : 1;
			heading.set(getVelocity().sub(desiredVelocity).setMag(-dir).add(headingCorrection));
			thrust = Math.max(-1, Math.min(1, dir * getVelocity().dist(desiredVelocity) / getSpeed()));
		}
	}

	public void keyPress(char key) {
		for(Module module : getModules()) {
			module.onKeyPress(this, key);
		}
		if(controlScheme == 0) {   // WASD
			switch(key) {
			case 'w':
				Resources.loopSound("engine");
				thrust = 1;
				autopilot = false;
				break;
			case 'a':
				turn = -1;
				autopilot = false;
				break;
			case 's':
				Resources.loopSound("engine");
				thrust = -1;
				autopilot = false;
				break;
			case 'd':
				turn = 1;
				autopilot = false;
				break;
			case '\t':
				autopilot = true;
				getWorld().updateTargeters(this);
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
				autopilot = false;
				break;
			case 'j':
				turn = -1;
				autopilot = false;
				break;
			case 'k':
				Resources.stopSound("engine");
				thrust = -1;
				autopilot = false;
				break;
			case 'l':
				turn = 1;
				autopilot = false;
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
		menu.add(new LoadoutMenuOption(this));
		menu.addDefault();
		setContext(menu);
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
