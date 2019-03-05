package vekta.object.ship;

import processing.core.PVector;
import vekta.Player;
import vekta.PlayerEvent;
import vekta.Resources;
import vekta.Vekta;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.*;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.Upgradeable;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.terrain.LandingSite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public abstract class ModularShip extends Ship implements Upgradeable {
	private Player controller;

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

	public final boolean hasController() {
		return getController() != null;
	}

	public final Player getController() {
		return controller;
	}

	public final void setController(Player player) {
		if(player == getController()) {
			return;
		}
		else if(hasController()) {
			getController().emit(PlayerEvent.CHANGE_SHIP, null);
		}
		this.controller = player;
		getController().emit(PlayerEvent.CHANGE_SHIP, this);
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
		if(hasController()) {
			getController().emit(PlayerEvent.INSTALL_MODULE, module);
		}
	}

	@Override
	public void removeModule(Module module) {
		if(modules.remove(module)) {
			getInventory().add(new ModuleItem(module));
			module.onUninstall(this);
			if(hasController()) {
				getController().emit(PlayerEvent.UNINSTALL_MODULE, module);
			}
		}
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

		if(hasController()) {
			// TODO: generalize menu shortcuts
			if(key == 'v' || key == 'q' || key == 'e') {
				Menu menu = openMenu();
				for(MenuOption option : menu.getOptions()) {
					boolean autoSelect = (key == 'q' && option instanceof MissionMenuOption)
							|| (key == 'e' && option instanceof LoadoutMenuOption);
					if(autoSelect) {
						option.select(menu);
						break;
					}
				}
			}
		}
	}

	public Menu openMenu() {
		Menu menu = new Menu(new ObjectMenuHandle(new BackOption(getWorld()), this));
		menu.add(new LoadoutMenuOption(this));
		menu.add(new MissionMenuOption(getController()));
		menu.addDefault();
		setContext(menu);

		return menu;
	}

	public void onKeyRelease(char key) {
		for(Module module : getModules()) {
			module.onKeyRelease(key);
		}
	}

	@Override
	public void onLand(LandingSite site) {
		if(hasController()) {
			Menu menu = new Menu(new LandingMenuHandle(site, getWorld()));
			for(Module m : getModules()) {
				m.onLandingMenu(site, menu);
			}
			site.getTerrain().setupLandingMenu(this, menu);
			menu.add(new SurveyOption(site));
			menu.addDefault();
			Resources.stopSound("engine");
			Resources.playSound("land");
			Vekta.setContext(menu);

			getController().emit(PlayerEvent.LAND, site);
		}
	}

	@Override
	public void onDock(SpaceObject s) {
		Resources.stopSound("engine");
		if(hasController()) {
			if(s instanceof Ship) {
				Menu menu = new Menu(new ObjectMenuHandle(new ShipUndockOption(this, getWorld()), s));
				((Ship)s).setupDockingMenu(this, menu);
				menu.addDefault();
				setContext(menu);
			}

			getController().emit(PlayerEvent.DOCK, s);
		}
	}

	@Override
	public void onDepart(SpaceObject obj) {
		setThrustControl(0);
		setTurnControl(0);
	}
}  
