package vekta.module;

import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.RechargeOption;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import static java.lang.Math.round;

public class BatteryModule extends ShipModule {
	private final int capacity;

	private float charge;

	public BatteryModule() {
		this(100);
	}

	public BatteryModule(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public float getCharge() {
		return getShip() != null ? getShip().getEnergy() : charge;
	}

	public float getRatio() {
		return getCharge() / getCapacity();
	}

	@Override
	public String getName() {
		return "Battery v" + ((float)getCapacity() / 100) + " (" + round(getRatio() * 100) + "%)";
	}

	@Override
	public ModuleType getType() {
		return ModuleType.BATTERY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof BatteryModule && getCapacity() > ((BatteryModule)other).getCapacity();
	}

	@Override
	public Module getVariant() {
		BatteryModule battery = new BatteryModule(chooseInclusive(1, 50) * 10);
		battery.charge = choose(0, battery.getCapacity());
		return battery;
	}

	@Override
	public void onInstall() {
		getShip().setEnergy(getCharge());
		getShip().setMaxEnergy(getCapacity());
	}

	@Override
	public void onUninstall() {
		charge = getShip().getEnergy();
		getShip().setMaxEnergy(0);
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();

			if(site.getTerrain().isInhabited() && getCharge() <= getCapacity() * .9F) {
				float price = .1F;
				for(Settlement settlement : site.getTerrain().getSettlements()) {
					if(settlement.getFaction().isAlly(menu.getPlayer().getFaction())) {
						price = 0;
						break;
					}
				}
				menu.add(new RechargeOption(getShip(), price));
			}
		}
	}
}
