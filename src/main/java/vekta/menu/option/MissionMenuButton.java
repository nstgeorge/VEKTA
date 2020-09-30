package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.MissionMenuHandle;
import vekta.mission.Mission;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class MissionMenuButton implements ButtonOption {

	public MissionMenuButton() {
	}

	@Override
	public String getName() {
		return "Missions";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new MissionMenuHandle());
		for(Mission mission : menu.getPlayer().getMissions()) {
			sub.add(new MissionButton(mission));
		}
		sub.add(menu.getPlayer().getCurrentMission() == null ? sub.getDefault() : new CustomButton("Deselect", m -> {
			m.getPlayer().setCurrentMission(null);
			m.close();
		}).withColor(v.color(200)));
		setContext(sub);
	}
}
