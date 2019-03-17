package vekta.menu.option;

import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.MissionMenuHandle;
import vekta.mission.Mission;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class MissionMenuOption implements MenuOption {
	private final Player player;

	public MissionMenuOption(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String getName() {
		return "Missions";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new MissionMenuHandle(menu.getDefault()));
		for(Mission mission : getPlayer().getMissions()) {
			sub.add(new MissionOption(mission));
		}
		sub.add(new CustomOption("Deselect", () -> {
			getPlayer().setCurrentMission(null);
			sub.close();
		}).withColor(v.color(200)));
		setContext(sub);
	}
}
