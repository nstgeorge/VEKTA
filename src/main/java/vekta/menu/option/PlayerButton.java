package vekta.menu.option;

import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.SpaceObjectMenuHandle;

import static vekta.Vekta.setContext;

public class PlayerButton implements ButtonOption {
	private final Player player;

	public PlayerButton(Player player) {
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
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), menu.getDefault(), new SpaceObjectMenuHandle(player.getShip()));
		sub.add(new TargetButton(player.getShip()));
		if(menu.getPlayer().getCurrentMission() != null) {
			sub.add(new CustomButton("Share Mission", m -> m.getPlayer().getCurrentMission().share(getPlayer()))
					.withRemoval());
		}
		sub.addDefault();
		setContext(sub);
	}
}
