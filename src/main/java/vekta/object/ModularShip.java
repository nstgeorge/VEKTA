package vekta.object;

import processing.core.PVector;
import vekta.Resources;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.object.module.Module;
import vekta.object.module.ModuleType;
import vekta.object.module.Upgradeable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ModularShip extends Ship implements Upgradeable {
	private boolean landing;
	private float thrust;
	private float turn;

	private float energy;
	private float maxEnergy;

	// Upgradeable modules
	private final List<Module> modules = new ArrayList<>();

	public ModularShip(String name, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(name, heading, position, velocity, color, speed, turnSpeed);
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
		if(thrust != 0 && hasEnergy()) {
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
		this.energy = energy;
		if(this.energy > maxEnergy) {
			this.energy = maxEnergy;
		}
	}

	public void addEnergy(float amount) {
		setEnergy(energy + amount);
	}

	public boolean consumeEnergy(float amount) {
		energy -= amount;
		if(energy < 0) {
			energy = 0;
			landing = true;
			return false;
		}
		return true;
	}

	public float getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(float amount) {
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

	@Override
	public List<Module> getModules() {
		return modules;
	}

	@Override
	public Module getModule(ModuleType type) {
		for(Module m : getModules()) {
			if(m.getType() == type) {
				return m;
			}
		}
		return null;
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

	public boolean isModuleTypeExclusive(ModuleType type) {
		return true;
	}

	@Override
	public void addModule(Module module) {
		// Ensure only one module per type when necessary
		if(isModuleTypeExclusive(module.getType())) {
			for(Module m : new ArrayList<>(modules)) {
				if(m.getType() == module.getType()) {
					removeModule(m);
				}
			}
		}
		// Remove corresponding item if found in inventory
		for(Item item : new ArrayList<>(getInventory().getItems())) {
			if(item instanceof ModuleItem && ((ModuleItem)item).getModule() == module) {
				getInventory().remove(item);
				break;
			}
		}
		modules.add(module);
		module.onInstall(this);
	}

	@Override
	public void removeModule(Module module) {
		System.out.println("#@%@%");
		if(modules.remove(module)) {
			getInventory().add(new ModuleItem(module));
		}
		module.onUninstall(this);
	}

	@Override
	public void onUpdate() {
		for(Module module : getModules()) {
			module.onUpdate();
		}
	}

	public void onKeyPress(char key) {
		for(Module module : getModules()) {
			module.onKeyPress(key);
		}
	}

	public void onKeyRelease(char key) {
		for(Module module : getModules()) {
			module.onKeyRelease(key);
		}
	}
}  
