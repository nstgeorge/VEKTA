package vekta.menu.option;

import com.google.common.collect.Lists;
import vekta.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.handle.EconomyMenuHandle;
import vekta.spawner.item.BondItemSpawner;

import java.util.List;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class BondMenuOption implements MenuOption {
	private final Inventory inventory;
	private final List<Faction> factions;

	public BondMenuOption(Inventory inv) {
		this(inv, Lists.newArrayList(getWorld().findObjects(Faction.class)));
	}

	public BondMenuOption(Inventory inv, List<Faction> factions) {
		this.inventory = inv;
		this.factions = factions;
	}

	@Override
	public String getName() {
		return "Bond Market";
	}

	public Inventory getInventory() {
		return inventory;
	}

	public List<Faction> getFactions() {
		return factions;
	}

	@Override
	public void select(Menu menu) {
		EconomyMenuHandle handle = new EconomyMenuHandle(new BackOption(menu), menu.getPlayer().getInventory(), this::update);
		Menu sub = new Menu(menu.getPlayer(), handle);
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
