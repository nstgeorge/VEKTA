package vekta.menu.option;

import vekta.player.Player;
import vekta.menu.Menu;
import vekta.object.ship.Rechargeable;

import static processing.core.PApplet.ceil;
import static processing.core.PApplet.min;
import static vekta.Vekta.moneyString;

public class RechargeButton implements ButtonOption {
	private final Player player;
	private final Rechargeable rechargeable;
	private final float price;
	
	public RechargeButton(Player player, float price) {
		this(player, player.getShip(), price);
	}

	public RechargeButton(Player player, Rechargeable rechargeable, float price) {
		this.player = player;
		this.rechargeable = rechargeable;
		this.price = price;
	}

	@Override
	public String getName() {
		return moneyString(rechargeable == player.getShip() ? "Recharge" : "Recharge " + rechargeable.getName(), getCost());
	}

	public int getCost() {
		return min(player.getInventory().getMoney(), ceil(price * rechargeable.getRechargeAmount()));
	}

	@Override
	public boolean isEnabled() {
		return player.getInventory().has(getCost()) && (price == 0 || player.getInventory().has(1));
	}

	@Override
	public void onSelect(Menu menu) {
		float rechargeAmount = price > 0 ? getCost() / price : rechargeable.getRechargeAmount();
		if(player.getInventory().remove(getCost())) {
			rechargeable.recharge(rechargeAmount);
			menu.remove(this);
		}
	}
}
