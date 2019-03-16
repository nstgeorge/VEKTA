package vekta.menu.option;

import com.google.common.collect.Lists;
import vekta.Faction;
import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;

import java.util.List;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class InternetMenuOption implements MenuOption {
	private final Player player;
	private final boolean enabled;

	public InternetMenuOption(Player player, boolean enabled) {
		this.player = player;
		this.enabled = enabled;
	}

	@Override
	public String getName() {
		return "Internet";
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void select(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new MenuHandle(new BackOption(menu)));
		
		sub.add(new BondMenuOption(menu.getPlayer().getInventory()));
		
		sub.addDefault();
		setContext(sub);
	}
}
