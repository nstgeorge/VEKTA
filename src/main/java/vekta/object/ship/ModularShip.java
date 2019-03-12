package vekta.object.ship;

import processing.core.PVector;
import vekta.*;
import vekta.context.World;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.*;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.ModuleUpgradeable;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.terrain.LandingSite;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.*;

public abstract class ModularShip extends Ship implements ModuleUpgradeable, PlayerListener {
	private static final float ENERGY_HEAT_SCALE = 1e4F;

	private Player controller;

	private boolean landing;
	private float thrust;
	private float turn;

	private float energy;
	private float maxEnergy;

	private final PVector acceleration = new PVector();

	// ModuleUpgradeable modules
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
		if(hasController()) {
			getController().emit(PlayerEvent.CHANGE_SHIP, null);
		}
		this.controller = player;
		player.addListener(this);
		getController().emit(PlayerEvent.CHANGE_SHIP, this);
	}

	@Override
	public RenderLevel getDespawnLevel() {
		//		return hasController();
		return RenderLevel.STAR;
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
		// TODO: adjust rate-based consumption for time acceleration
		addHeat(amount * ENERGY_HEAT_SCALE);
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
				Module module = ((ModuleItem)item).getModule();
				if(module.isApplicable(this)) {
					list.add(module);
				}
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
		// Remove corresponding item_common.txt if found in inventory
		for(Item item : getInventory()) {
			if(item instanceof ModuleItem && ((ModuleItem)item).getModule() == module) {
				getInventory().remove(item);
				break;
			}
		}
		modules.add(module);
		module.onInstall(this);
		if(hasController()) {
			//			if(module instanceof PlayerListener) {
			//				getController().addListener((PlayerListener)module);
			//			}
			getController().emit(PlayerEvent.INSTALL_MODULE, module);
		}
	}

	@Override
	public void removeModule(Module module) {
		if(modules.remove(module)) {
			getInventory().add(new ModuleItem(module));
			module.onUninstall(this);
			if(hasController()) {
				//				if(module instanceof PlayerListener) {
				//					getController().removeListener((PlayerListener)module);
				//				}
				getController().emit(PlayerEvent.UNINSTALL_MODULE, module);
			}
		}
	}

	public PVector getAcceleration() {
		return acceleration;
	}

	@Override
	public PVector applyGravity(List<SpaceObject> objects) {
		this.acceleration.set(super.applyGravity(objects));
		return this.acceleration;
	}

	@Override
	public void draw(RenderLevel level, float r) {
		float t = getWorld().getTimeScale();
		if(!getRenderLevel().isVisibleTo(level) && hasController()) {
			// Draw acceleration vector
			v.stroke(255, 0, 0);
			v.line(0, 0, (acceleration.x * 100 / t), (acceleration.y * 100 / t));
		}

		v.stroke(getColor());
		super.draw(level, r);
	}

	@Override
	public void onUpdate(RenderLevel level) {
		for(Module module : getModules()) {
			module.onUpdate();
		}
	}

	@Override
	public void updateTargets() {
		World world = getWorld();
		for(Module m : getModules()) {
			if(m instanceof Targeter) {
				world.updateTargeter((Targeter)m);
			}
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		if(hasController()) {
			getWorld().setDead();// TODO: convert to event
		}
	}

	public Menu openShipMenu() {
		Menu menu = new Menu(getController(), new ObjectMenuHandle(new BackOption(getWorld()), this));
		menu.add(new LoadoutMenuOption(this));
		menu.add(new MissionMenuOption(getController()));
		menu.add(new RenameOption(this));
		menu.addDefault();
		setContext(menu);

		return menu;
	}

	@Override
	public void doLand(LandingSite site) {
		if(hasController()) {
			Menu menu = new Menu(getController(), new LandingMenuHandle(site, getWorld()));
			site.getTerrain().setupLandingMenu(menu);
			menu.add(new SurveyOption(site));
			menu.addDefault();
			Resources.stopSound("engine");
			Resources.playSound("land");
			Vekta.setContext(menu);

			getController().emit(PlayerEvent.LAND, site);
		}
	}

	@Override
	public void doDock(SpaceObject s) {
		Resources.stopSound("engine");
		if(hasController()) {
			if(s instanceof Ship) {
				Menu menu = new Menu(getController(), new ObjectMenuHandle(new ShipUndockOption(this, getWorld()), s));
				((Ship)s).setupDockingMenu(getController(), menu);
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

	//// InventoryListener hooks

	@Override
	public void onMoneyAdd(int amount) {
		//		if(hasController()) {
		//			getController().send("+ " + amount + " G").withTime(.5F);
		//		}
	}

	@Override
	public void onMoneyRemove(int amount) {
		//		if(hasController()) {
		//			getController().send("- " + amount + " G").withTime(.5F);
		//		}
	}

	@Override
	public void onItemAdd(Item item) {
		if(hasController()) {
			getController().emit(PlayerEvent.ADD_ITEM, item);
		}
	}

	@Override
	public void onItemRemove(Item item) {
		if(hasController()) {
			getController().emit(PlayerEvent.REMOVE_ITEM, item);
		}
	}
	
	//// Syncable overrides

//	@Override
	//	public boolean shouldSyncField(Field field) {
	//		return !"controller".equals(field.getName()); //// TODO: prevent cycles differently
	//	}

	//// PlayerListener callbacks, active when hasController() == true

	@Override
	public void onChangeShip(ModularShip ship) {
		if(ship != this) {
			getController().removeListener(this);
		}
	}

	@Override
	public void onMenu(Menu menu) {
		for(Item item : getInventory()){
			item.onMenu(menu);
		}
		for(Module module : getModules()) {
			module.onItemMenu(menu);
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		for(Module module : getModules()) {
			module.onKeyPress(key);
		}

		// TODO: generalize menu shortcuts
		if(key == KeyBinding.SHIP_MENU || key == KeyBinding.SHIP_MISSIONS || key == KeyBinding.SHIP_LOADOUT) {
			Menu menu = openShipMenu();
			for(MenuOption option : menu.getOptions()) {
				boolean autoSelect = (key == KeyBinding.SHIP_MISSIONS && option instanceof MissionMenuOption)
						|| (key == KeyBinding.SHIP_LOADOUT && option instanceof LoadoutMenuOption);
				if(autoSelect) {
					option.select(menu);
					break;
				}
			}
		}
	}

	@Override
	public void onKeyRelease(KeyBinding key) {
		for(Module module : getModules()) {
			module.onKeyRelease(key);
		}
	}
}  
