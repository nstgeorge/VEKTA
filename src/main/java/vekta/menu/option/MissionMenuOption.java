package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.MissionMenuHandle;
import vekta.mission.Mission;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class MissionMenuOption implements MenuOption {

	public MissionMenuOption() {
	}

	@Override
	public String getName() {
		return "Missions";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), menu.getDefault(), new MissionMenuHandle());
		for(Mission mission : menu.getPlayer().getMissions()) {
			sub.add(new MissionOption(mission));
		}
		sub.add(new CustomOption("Deselect", m -> {
			m.getPlayer().setCurrentMission(null);
			m.close();
		}).withColor(v.color(200)));
		setContext(sub);
	}
}
