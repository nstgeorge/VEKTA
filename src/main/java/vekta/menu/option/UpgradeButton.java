package vekta.menu.option;

import vekta.player.Player;
import vekta.menu.Menu;
import vekta.terrain.settlement.building.upgrade.SettlementUpgrade;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.moneyString;

public class UpgradeButton extends ButtonOption {
	private final Player player;
	private final Settlement settlement;
	private final SettlementUpgrade upgrade;

	public UpgradeButton(Player player, Settlement settlement, SettlementUpgrade upgrade) {
		this.player = player;
		this.settlement = settlement;
		this.upgrade = upgrade;
	}

	@Override
	public String getName() {
		return moneyString(upgrade.getName(), upgrade.getCost(player, settlement));
	}

	@Override
	public boolean isEnabled() {
		return player.getInventory().has(upgrade.getCost(player, settlement));
	}

	@Override
	public void onSelect(Menu menu) {
		int cost = upgrade.getCost(player, settlement);
		if(player.getInventory().remove(cost)) {
			upgrade.upgrade(player, settlement);
			menu.close();
		}
	}
}
