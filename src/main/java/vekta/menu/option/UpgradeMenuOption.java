package vekta.menu.option;

import vekta.Player;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.terrain.building.upgrade.SettlementUpgrade;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.setContext;

public class UpgradeMenuOption implements MenuOption {
	private static final SettlementUpgrade[] UPGRADES = Resources.getSubclassInstances(SettlementUpgrade.class);

	private final Player player;
	private final Settlement settlement;

	public UpgradeMenuOption(Player player, Settlement settlement) {
		this.player = player;
		this.settlement = settlement;
	}

	@Override
	public String getName() {
		return "Upgrade";
	}

	@Override
	public void select(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), menu.getHandle());
		updateMenu(sub, menu);
		sub.addSelectListener(option -> updateMenu(sub, menu));
		setContext(sub);
	}

	private void updateMenu(Menu sub, Menu parent) {
		sub.clear();
		for(SettlementUpgrade upgrade : UPGRADES) {
			if(upgrade.isAvailable(player, settlement)) {
				sub.add(new UpgradeOption(player, settlement, upgrade));
			}
		}
		sub.add(new BackOption(parent));
	}
}
