package vekta.object.ship;

import com.google.common.collect.ImmutableMap;
import processing.core.PVector;
import vekta.*;
import vekta.context.World;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.*;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.ModuleUpgradeable;
import vekta.module.TargetingModule;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static vekta.Vekta.*;

public abstract class ModularShip extends Ship implements ModuleUpgradeable, PlayerListener, Rechargeable {
	private static final Map<KeyBinding, Class<? extends ButtonOption>> SHORTCUT_MAP = ImmutableMap.of(
			KeyBinding.SHIP_MISSIONS, MissionMenuButton.class,
			KeyBinding.SHIP_LOADOUT, LoadoutMenuButton.class,
			KeyBinding.SHIP_INTERNET, InternetMenuButton.class,
			KeyBinding.SHIP_INVENTORY, InventoryButton.class,
			KeyBinding.SHIP_KNOWLEDGE, PlayerKnowledgeButton.class
	);

	private static final float ENERGY_TIME_SCALE = 1e-4F;
	private static final float ENERGY_HEAT_SCALE = 1;

	private Player controller;

	private boolean landing;
	private float thrust;
	private float turn;

	private float mass;

	private boolean overheated;
	private final List<Battery> batteries = new ArrayList<>();
	private float energy;
	private float maxEnergy;

	private final PVector acceleration = new PVector();

	private final List<Module> modules = new ArrayList<>();

	public ModularShip(String name, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(name, heading, position, velocity, color, speed, turnSpeed);
	}

	@Override
	public float getMass() {
		return mass;
	}

	public float getBaseMass() {
		return 1000;
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
			getController().removeListener(this);
		}
		this.controller = player;
		if(hasController()) {
			getController().addListener(this);
			getController().emit(PlayerEvent.CHANGE_SHIP, this);
		}
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

	public float getOverheatTemperature() {
		return 50;
	}

	public float getCooldownTemperature() {
		return 45;
	}

	public boolean isOverheated() {
		return overheated;
	}

	public List<Battery> getBatteries() {
		return batteries;
	}

	public boolean hasBattery(Battery battery) {
		return batteries.contains(battery);
	}

	public void addBattery(Battery battery) {
		if(!hasBattery(battery)) {
			batteries.add(battery);
			maxEnergy += battery.getCapacity();
			energy += battery.getCharge();
			battery.setCharge(0);
		}
	}

	public void removeBattery(Battery battery) {
		if(hasBattery(battery)) {
			batteries.remove(battery);
			maxEnergy -= battery.getCapacity();
			float chargeTransfer = max(0, energy - battery.getCapacity());
			energy -= chargeTransfer;
			battery.setCharge(chargeTransfer);
		}
	}

	public boolean hasEnergy() {
		return getEnergy() > 0 && !isOverheated();
	}

	public float getEnergy() {
		return energy;
	}

	public void setEnergy(float energy) {
		this.energy = energy;
	}

	public float getMaxEnergy() {
		return maxEnergy;
	}

	@Override
	public float getRechargeAmount() {
		return getMaxEnergy() - getEnergy();
	}

	@Override
	public void recharge(float amount) {
		energy += amount;
		if(energy > maxEnergy) {
			energy = maxEnergy;
		}
	}

	public boolean consumeEnergyOverTime(float amount) {
		return consumeEnergyImmediate(amount * ENERGY_TIME_SCALE * getWorld().getTimeScale());
	}

	public boolean consumeEnergyImmediate(float amount) {
		if(getTemperature() >= getOverheatTemperature()) {
			overheated = true;
			return false;
		}
		else if(overheated && getTemperature() <= getCooldownTemperature()) {
			overheated = false;
		}

		addHeat(amount * ENERGY_HEAT_SCALE / ENERGY_TIME_SCALE);
		energy -= amount;
		if(energy <= 0) {
			energy = 0;
			landing = true;
			return false;
		}
		return true;
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
			getController().emit(PlayerEvent.INSTALL_MODULE, module);
		}
		updateMass();
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
		updateMass();
	}

	private void updateMass() {
		mass = getBaseMass();
		for(Module module : getModules()) {
			mass += module.getMass();
		}
		for(Item item : getInventory().getItems()) {
			mass += item.getMass();
		}
	}

	public PVector getAccelerationReference() {
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
	public void drawShipMarker(RenderLevel level, float r) {
		if(getController() != null) {
			return;
		}

		super.drawShipMarker(level, r);
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
	public boolean damage(float amount, Damager damager) {
		if(hasController()) {
			DamageAttempt attempt = new DamageAttempt(amount, damager);
			getController().emit(PlayerEvent.DAMAGE_SHIP, attempt);
			for(Module m : getModules()) {
				m.onDamageShip(attempt);
			}
			amount = attempt.getAmount();
			damager = attempt.getDamager();
		}
		return super.damage(amount, damager);
	}

	// TODO: setValue modules/UI to use these methods

	public SpaceObject findNavigationTarget() {
		Module m = getModule(ModuleType.NAVIGATION);
		if(m instanceof Targeter) {
			return ((Targeter)m).getTarget();
		}
		return null;
	}

	public void setNavigationTarget(SpaceObject target) {
		Module m = getModule(ModuleType.NAVIGATION);
		if(m instanceof TargetingModule) { // Reset targeting mode if using a TargetingModule
			((TargetingModule)m).setMode(null);
		}
		if(m instanceof Targeter) {
			((Targeter)m).setTarget(target);
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		if(hasController()) {
			getWorld().setDead();// TODO: convert to event
		}
	}

	public void openShipMenu() {
		Menu menu = new Menu(getController(), new BackButton(getWorld()), new SpaceObjectMenuHandle(this));
		menu.add(new PlayerKnowledgeButton());
		menu.add(new InventoryButton(getInventory()));
		menu.add(new LoadoutMenuButton(this));
		menu.add(new MissionMenuButton());
		menu.add(new RenameButton(this));
		menu.addDefault();
		setContext(menu);
	}

	@Override
	public void doLand(LandingSite site) {
		if(hasController()) {
			Player player = getController();
			Menu menu = new Menu(player, new ShipTakeoffButton(site, getWorld()), new LandingMenuHandle(site));
			for(Settlement settlement : site.getTerrain().getSettlements()) {
				menu.add(new SettlementButton(settlement));
			}
			site.getTerrain().setupLandingMenu(site, menu);
			menu.add(new SurveyButton(site));
			menu.addDefault();
			Resources.playSound("land");
			this.setLanding(false);
			setContext(menu);

			player.emit(PlayerEvent.LAND, site);
			site.getParent().observe(ObservationLevel.VISITED, player);
		}
	}

	@Override
	public void doDock(SpaceObject s) {
		if(hasController()) {
			Player player = getController();
			if(s instanceof Ship) {
				this.setLanding(false);
				Menu menu = new Menu(player, new ShipUndockButton(this, getWorld()), new SpaceObjectMenuHandle(s));
				((Ship)s).setupDockingMenu(menu);
				menu.addDefault();
				setContext(menu);
			}

			player.emit(PlayerEvent.DOCK, s);
			s.observe(ObservationLevel.VISITED, player);
		}
	}

	@Override
	public void onDepart(SpaceObject obj) {
		setThrustControl(0);
		setTurnControl(0);
	}

	@Override
	public void setupDockingMenu(Menu menu) {
		// TODO: add ShipSwitchButton when relevant
	}

	//// InventoryListener hooks

	@Override
	public void onItemAdd(Item item) {
		if(hasController()) {
			getController().emit(PlayerEvent.ADD_ITEM, item);
		}
		updateMass();
	}

	@Override
	public void onItemRemove(Item item) {
		if(hasController()) {
			getController().emit(PlayerEvent.REMOVE_ITEM, item);
		}
		updateMass();
	}

	//// PlayerListener callbacks, active when hasController() == true

	@Override
	public void onMenu(Menu menu) {
		for(Item item : getInventory()) {
			item.onMenu(menu);
		}
		for(Module module : getModules()) {
			module.onMenu(menu);
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		for(Module module : getModules()) {
			module.onKeyPress(key);
		}

		Class<?> shortcutType = SHORTCUT_MAP.get(key);
		if(key == KeyBinding.SHIP_MENU || shortcutType != null) {
			openShipMenu();
			applyContext();
			Menu menu = (Menu)getContext();
			if(shortcutType != null) {
				for(MenuOption option : menu.getOptions()) {
					if(shortcutType.isInstance(option) && option.isEnabled()) {
						menu.select(option);
						break;
					}
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

	@Override
	public void onSync(SpaceObject data) {
		super.onSync(data);

		if(isRemote()) {
			controller = null;
		}
	}

	/**
	 * Battery abstraction for modular ships.
	 */
	public static final class Battery implements Serializable {
		private final int capacity;

		private float charge;

		public Battery(int capacity) {
			this.capacity = capacity;
		}

		public int getCapacity() {
			return capacity;
		}

		public float getCharge() {
			return charge;
		}

		public void setCharge(float charge) {
			this.charge = charge;
		}

		public void addCharge(float amount) {
			charge += amount;
			if(charge > capacity) {
				charge = capacity;
			}
		}

		public float getRatio() {
			return getCharge() / getCapacity();
		}
	}

	public static class DamageAttempt {
		private float amount;
		private Damager damager;

		public DamageAttempt(float amount, Damager damager) {
			this.amount = amount;
			this.damager = damager;
		}

		public float getAmount() {
			return amount;
		}

		public void setAmount(float amount) {
			this.amount = amount;
		}

		public Damager getDamager() {
			return damager;
		}

		public void setDamager(Damager damager) {
			this.damager = damager;
		}
	}
}  
