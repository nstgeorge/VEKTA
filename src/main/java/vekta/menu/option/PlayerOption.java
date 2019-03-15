package vekta.menu.option;

import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.module.ModuleType;
import vekta.object.Targeter;

import static vekta.Vekta.setContext;

public class PlayerOption implements MenuOption {
	private final Player player;

	public PlayerOption(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String getName() {
		return getPlayer().getName();
	}

	@Override
	public int getColor() {
		return getPlayer().getColor();
	}

	@Override
	public void select(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new ObjectMenuHandle(menu.getDefault(), player.getShip()));
		sub.add(new CustomOption("Set Target", m -> {
			Targeter t = (Targeter)m.getPlayer().getShip().getModule(ModuleType.TARGET_COMPUTER);
			if(t != null) {
				t.setTarget(player.getShip());
			}
		}).withRemoval());
		if(menu.getPlayer().getCurrentMission() != null) {
			sub.add(new CustomOption("Share Mission", m -> m.getPlayer().getCurrentMission().share(getPlayer()))
					.withRemoval());
		}
		sub.addDefault();
		setContext(sub);
	}
}
