package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

public class RechargeOption implements MenuOption {
	private final ModularShip ship;
	private final float price;

	public RechargeOption(ModularShip ship, float price) {
		this.ship = ship;
		this.price = price;
	}

	@Override
	public String getName() {
		int cost = getCost();
		return "Recharge" + (cost > 0 ? " [" + cost + " G]" : "");
	}

	public int getCost() {
		return Math.round(price * (1 - ship.getEnergy() / ship.getMaxEnergy()));
	}

	@Override
	public boolean isEnabled(Menu menu) {
		return ship.getInventory().has(getCost());
	}

	@Override
	public void select(Menu menu) {
		ship.recharge();
		if(ship.getEnergy() == ship.getMaxEnergy()) {
			menu.remove(this);
		}
	}
}
