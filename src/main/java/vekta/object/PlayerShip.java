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

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class PlayerShip extends ControllableShip implements Upgradeable {
	// Default PlayerShip stuff
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 20; // Base turn speed (RCS turnSpeed = 1)

	private final PVector influence = new PVector();

	// Upgradeable modules
	private final List<Module> modules = new ArrayList<>();

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, DEF_MASS, DEF_RADIUS, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		// Default modules
		addModule(new EngineModule(1));
		addModule(new RCSModule(1));
		addModule(new TargetingModule());
		addModule(new BatteryModule(100));
		addModule(new CannonModule());

		setEnergy(getMaxEnergy() * .2F);
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
			module.onUpdate();
		}
	}

	public void onKeyPress(char key) {
		for(Module module : getModules()) {
			module.onKeyPress(key);
		}
		if(key == 'e') {
			openMenu();
		}
	}

	public void onKeyRelease(char key) {
		for(Module module : getModules()) {
			module.onKeyRelease(key);
		}
	}

	public void openMenu() {
		Menu menu = new Menu(new ObjectMenuHandle(new BackOption(getWorld()), this));
		menu.add(new LoadoutMenuOption(this));
		menu.addDefault();
		setContext(menu);
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
		setThrustControl(0);
		setTurnControl(0);
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
