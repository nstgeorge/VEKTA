package vekta.menu.option;

import com.google.common.collect.Lists;
import vekta.Faction;
import vekta.menu.Menu;
import vekta.menu.handle.EconomyMenuHandle;
import vekta.spawner.item.BondItemSpawner;

import java.util.List;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class BondMenuOption implements MenuOption {
	private final List<Faction> factions;

	public BondMenuOption() {
		this(Lists.newArrayList(getWorld().findObjects(Faction.class)));
	}

	public BondMenuOption(List<Faction> factions) {
		this.factions = factions;
	}

	@Override
	public String getName() {
		return "Bond Market";
	}

	public List<Faction> getFactions() {
		return factions;
	}

	@Override
	public void onSelect(Menu menu) {
		EconomyMenuHandle handle = new EconomyMenuHandle(menu.getPlayer().getInventory(), this::update);
		Menu sub = new Menu(menu, handle);
		update(sub, handle.isBuying());
		setContext(sub);
	}

	private void update(Menu sub, boolean buying) {
		sub.clear();
		for(Faction faction : getFactions()) {
			float valueChange = .2F; // Amount to raise the market value
			sub.add(new EconomyItemOption(
					sub.getPlayer().getInventory(),
					BondItemSpawner.createBondItem(faction),
					valueChange,
					buying,
					this::update));
		}
		sub.addDefault();
	}
}
