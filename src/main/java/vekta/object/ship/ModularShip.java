package vekta.object.ship;

import com.google.common.collect.ImmutableMap;
import processing.core.PVector;
import vekta.*;
import vekta.module.*;
import vekta.object.planet.Planet;
import vekta.object.planet.TerrestrialPlanet;
import vekta.situation.NearPlanetSituation;
import vekta.world.World;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.*;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.player.Player;
import vekta.player.PlayerEvent;
import vekta.player.PlayerListener;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;
import vekta.world.RenderLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static vekta.Vekta.*;

public abstract class ModularShip extends Ship implements ModuleUpgradeable, PlayerListener, Rechargeable {
	private static final Map<KeyBinding, Class<? extends MenuOption>> SHORTCUT_MAP = ImmutableMap.<KeyBinding, Class<? extends MenuOption>>builder()
			.put(KeyBinding.SHIP_KNOWLEDGE, PlayerKnowledgeButton.class)
			.put(KeyBinding.SHIP_MISSIONS, MissionMenuButton.class)
			.put(KeyBinding.SHIP_LOADOUT, LoadoutMenuButton.class)
			.put(KeyBinding.SHIP_INTERNET, InternetMenuButton.class)
			.put(KeyBinding.SHIP_FOLLOWERS, FollowerMenuButton.class)
			.put(KeyBinding.SHIP_INVENTORY, InventoryButton.class)
			.build();

	private static final float ENERGY_TIME_SCALE = 1e-4F;
	private static final float ENERGY_HEAT_SCALE = 1;

	private Player controller;

	private boolean landing;
	private float thrust;
	private float turn;
	private boolean hyperdriving;

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
		return 10000;
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

	public float getMaxZoomLevel() {
		return isHyperdriving() ? INTERSTELLAR_LEVEL : STAR_LEVEL * .99F; // Slightly below STAR_LEVEL to pacify smooth zooming
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
		device.setVibration((int)(Math.abs(thrust) * 65535 * Settings.getFloat("rumbleAmount")), (int)(Math.abs(thrust) * 65535 * Settings.getFloat("rumbleAmount")));
		this.thrust = thrust;
		if(!isHyperdriving() && hasController()) {
			if(thrust != 0 && hasEnergy()) {
				Resources.loopSound("engine", thrust);
			}
			else {
				Resources.stopSound("engine");
			}
		}
	}

	public float getTurnControl() {
		return turn;
	}

	public void setTurnControl(float turn) {
		this.turn = turn;
	}

	public boolean isHyperdriving() {
		return hyperdriving;
	}

	public void setHyperdriving(boolean hyperdriving) {
		this.hyperdriving = hyperdriving;
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
			if(energy > maxEnergy) {
				energy = maxEnergy;
			}
			battery.setCharge(0);
		}
	}

	public void removeBattery(Battery battery) {
		if(hasBattery(battery)) {
			batteries.remove(battery);
			maxEnergy -= battery.getCapacity();
			float chargeTransfer = min(energy, battery.getCapacity());
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
		boolean exclusive = isModuleTypeExclusive(module.getType());
		for(Module m : new ArrayList<>(modules)) {
			if(module == m) {
				return;
			}
			if(exclusive && m.getType() == module.getType()) {
				removeModule(m);
			}
		}
		// Remove corresponding item if found in inventory
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

//		if(hasController()) {
//			if(getController().hasAttribute(NearPlanetSituation.class)) {
//
//			}
//		}

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

	public SpaceObject findNavigationTarget() {
		for(Module m : getModules()) {
			if(m.getType() == ModuleType.NAVIGATION && m instanceof Targeter) {
				return ((Targeter)m).getTarget();
			}
		}
		return null;
	}

	public void setNavigationTarget(SpaceObject target) {
		for(Module m : getModules()) {
			if(m.getType() == ModuleType.NAVIGATION) {
				if(m instanceof TargetingModule) { // Reset targeting mode if using a TargetingModule
					((TargetingModule)m).setMode(null);
				}
				if(m instanceof Targeter) {
					((Targeter)m).setTarget(target);
				}
				return; // Only adjust first navigation module
			}
		}
	}

	@Override
	public void onDestroyed(SpaceObject s) {
		if(hasController()) {
			getController().emit(PlayerEvent.GAME_OVER, this);
		}
	}

	public Menu createShipMenu() {
		Menu menu = new Menu(getController(), new BackButton(getWorld()), new SpaceObjectMenuHandle(this));
		menu.add(new PlayerKnowledgeButton());
		menu.add(new MissionMenuButton());
		menu.add(new LoadoutMenuButton(this));
		menu.add(new InventoryButton(getInventory()));
		if(hasController()) {
			FollowerMenuButton followerButton = new FollowerMenuButton(getController());
			menu.add(followerButton);
		}
		menu.add(new RenameButton(this));
		menu.addDefault();
		return menu;
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

		if(key == KeyBinding.SHIP_MENU) {
			setContext(createShipMenu());
		}
		else {
			Class<? extends MenuOption> type = SHORTCUT_MAP.get(key);
			if(type != null) {
				Menu menu = createShipMenu();
				for(MenuOption opt : menu.getOptions()) {
					if(type.isInstance(opt)) {
						menu.select(opt);
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

		public Battery(int capacity, boolean charged) {
			this(capacity);

			if(charged) {
				setCharge(capacity);
			}
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

	private interface ShortcutProvider extends Serializable {
		MenuOption provide(ModularShip ship, Menu menu);
	}
}  
