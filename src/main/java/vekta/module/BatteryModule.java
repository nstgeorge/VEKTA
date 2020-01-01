package vekta.module;

import vekta.util.InfoGroup;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.RechargeButton;
import vekta.object.ship.ModularShip;
import vekta.object.ship.Rechargeable;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import java.util.List;

import static processing.core.PApplet.round;
import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class BatteryModule extends ShipModule implements Rechargeable {
	private static final float CHARGE_THRESHOLD = .9F;

	private final ModularShip.Battery battery;

	public BatteryModule() {
		this(new ModularShip.Battery(100));
	}

	public BatteryModule(ModularShip.Battery battery) {
		this.battery = battery;
	}

	public ModularShip.Battery getBattery() {
		return battery;
	}

	@Override
	public String getName() {
		return "Battery v" + ((float)getBattery().getCapacity() / 100) + (getShip() != null ? " (" + round(getBattery().getRatio() * 100) + "%)" : "");
	}

	@Override
	public float getRechargeAmount() {
		return getBattery().getCapacity() - getBattery().getCharge();
	}

	@Override
	public void recharge(float amount) {
		getBattery().addCharge(amount);
	}

	@Override
	public ModuleType getType() {
		return ModuleType.BATTERY;
	}

	@Override
	public int getMass() {
		return 10 * getBattery().getCapacity();
	}

	@Override
	public float getValueScale() {
		return getBattery().getCapacity() / 100F;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof BatteryModule && getBattery().getCapacity() > ((BatteryModule)other).getBattery().getCapacity();
	}

	@Override
	public Module createVariant() {
		ModularShip.Battery battery = new ModularShip.Battery(chooseInclusive(10, 500));
		battery.setCharge(sq(v.random(1)) * battery.getCapacity());
		return new BatteryModule(battery);
	}

	@Override
	public void onInstall() {
		getShip().addBattery(getBattery());
	}

	@Override
	public void onUninstall() {
		getShip().removeBattery(getBattery());
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();

			if(site.getTerrain().isInhabited()) {
				float price = .5F;
				for(Settlement settlement : site.getTerrain().getSettlements()) {
					if(settlement.getFaction().isAlly(menu.getPlayer().getFaction())) {
						price = .1F;
						break;
					}
				}

				ModularShip ship = menu.getPlayer().getShip();
				List<ModularShip.Battery> shipBatteries = ship.getBatteries();
				if(shipBatteries.contains(getBattery()) && shipBatteries.get(0) == getBattery()) {
					// Add recharge option for ship rather than mounted battery
					if(ship.getEnergy() <= ship.getMaxEnergy() * CHARGE_THRESHOLD) {
						menu.add(new RechargeButton(menu.getPlayer(), price));
					}
				}
				else if(getBattery().getCharge() <= getBattery().getCapacity() * CHARGE_THRESHOLD) {
					menu.add(new RechargeButton(menu.getPlayer(), this, price));
				}
			}
		}
	}

	@Override
	public void onItemMenu(Item item, Menu menu) {
		onMenu(menu);
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Batteries are the core of any modern spacecraft. The more energy storage, the better.");
	}
}
