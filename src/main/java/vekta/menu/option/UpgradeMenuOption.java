package vekta.menu.option;

import vekta.Player;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.terrain.building.upgrade.SettlementUpgrade;
import vekta.terrain.settlement.Settlement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new MenuHandle());
		updateMenu(sub, menu);
		sub.addSelectListener(option -> updateMenu(sub, menu));
		setContext(sub);
	}

	private void updateMenu(Menu sub, Menu parent) {
		sub.clear();

		List<SettlementUpgrade> upgrades = new ArrayList<>(Arrays.asList(UPGRADES));
		upgrades.sort(Comparator.comparingInt(u -> u.getCost(sub.getPlayer(), settlement)));

		for(SettlementUpgrade upgrade : upgrades) {
			if(upgrade.isAvailable(player, settlement)) {
				sub.add(new UpgradeOption(player, settlement, upgrade));
			}
		}
		sub.addDefault();
	}
}
