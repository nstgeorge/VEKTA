package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

import static processing.core.PApplet.ceil;
import static processing.core.PApplet.min;
import static vekta.Vekta.moneyString;

public class RechargeOption implements MenuOption {
	private final ModularShip ship;
	private final float price;

	public RechargeOption(ModularShip ship, float price) {
		this.ship = ship;
		this.price = price;
	}

	@Override
	public String getName() {
		return moneyString("Recharge", getCost());
	}

	public int getCost() {
		return min(ship.getInventory().getMoney(), ceil(price * (ship.getMaxEnergy() - ship.getEnergy())));
	}

	@Override
	public boolean isEnabled() {
		return ship.getInventory().has(getCost());
	}

	@Override
	public void select(Menu menu) {
		if(ship.getInventory().remove(getCost())) {
			ship.addEnergy(getCost() / price);
			menu.remove(this);
		}
	}
}
