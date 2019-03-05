package vekta.menu.option;

import vekta.Player;
import vekta.menu.Menu;
import vekta.mission.Mission;
import vekta.mission.MissionStatus;

import static vekta.Vekta.MISSION_COLOR;

public class MissionOption implements MenuOption {
	private final Player player;
	private final Mission mission;

	public MissionOption(Player player, Mission mission) {
		this.player = player;
		this.mission = mission;
	}

	public Player getPlayer() {
		return player;
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
		if(getPlayer().getCurrentMission() == getMission()) {
			return MISSION_COLOR;
		}
		return getMission().getStatus().getColor();
	}

	@Override
	public void select(Menu menu) {
		if(mission.getStatus() == MissionStatus.READY) {
			getMission().start(getPlayer());
		}
		else if(mission.getStatus() == MissionStatus.STARTED) {
			getPlayer().setCurrentMission(mission);
		}
		menu.close();
	}
}
