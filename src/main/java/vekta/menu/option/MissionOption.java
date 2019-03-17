package vekta.menu.option;

import vekta.menu.Menu;
import vekta.mission.Mission;
import vekta.mission.MissionStatus;

import static vekta.Vekta.MISSION_COLOR;

public class MissionOption implements MenuOption {
	private final Mission mission;

	public MissionOption(Mission mission) {
		this.mission = mission;
	}

	public Mission getMission() {
		return mission;
	}

	@Override
	public String getName() {
		return getMission().getName();
	}

	@Override
	public int getColor() {
		if(getMission().getPlayer() != null && getMission().getPlayer().getCurrentMission() == getMission()) {
			return MISSION_COLOR;
		}
		return getMission().getStatus().getColor();
	}

	@Override
	public void onSelect(Menu menu) {
		if(mission.getStatus() == MissionStatus.READY) {
			getMission().start();
		}
		else if(mission.getStatus() == MissionStatus.STARTED) {
			menu.getPlayer().setCurrentMission(mission);
		}
		menu.close();
	}
}
