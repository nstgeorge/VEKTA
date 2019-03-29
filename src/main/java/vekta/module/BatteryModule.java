package vekta.module;

import vekta.InfoGroup;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.RechargeOption;
import vekta.object.ship.ModularShip;
import vekta.object.ship.Rechargeable;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import java.util.List;

import static processing.core.PApplet.round;
import static processing.core.PApplet.sq;

public class BatteryModule extends ShipModule implements Rechargeable {
	private static float CHARGE_THRESHOLD = .9F;

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
		return "Battery v" + ((float)getBattery().getCapacity() / 100) + " (" + round(getBattery().getRatio() * 100) + "%)";
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
	public boolean isBetter(Module other) {
		return other instanceof BatteryModule && getBattery().getCapacity() > ((BatteryModule)other).getBattery().getCapacity();
	}

	@Override
	public Module getVariant() {
		ModularShip.Battery battery = new ModularShip.Battery(chooseInclusive(1, 50) * 10);
		battery.setCharge(sq(choose(0, 1)) * battery.getCapacity());
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
				float price = .2F;
				for(Settlement settlement : site.getTerrain().getSettlements()) {
					if(settlement.getFaction().isAlly(menu.getPlayer().getFaction())) {
						price = 0;
						break;
					}
				}

				ModularShip ship = menu.getPlayer().getShip();
				List<ModularShip.Battery> shipBatteries = ship.getBatteries();
				if(shipBatteries.contains(getBattery()) && shipBatteries.get(0) == getBattery()) {
					// Add recharge option for ship rather than mounted battery
					if(ship.getEnergy() <= ship.getMaxEnergy() * CHARGE_THRESHOLD) {
						menu.add(new RechargeOption(menu.getPlayer(), price));
					}
				}
				else if(getBattery().getCharge() <= getBattery().getCapacity() * CHARGE_THRESHOLD) {
					menu.add(new RechargeOption(menu.getPlayer(), this, price));
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
