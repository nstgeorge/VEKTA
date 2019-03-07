package vekta.item;

import vekta.mission.Mission;

public class MissionItem extends Item {
	private final Mission mission;

	public MissionItem(String name, Mission mission) {
		super(name, ItemType.MISSION);
		
		this.mission = mission;
	}

	public Mission getMission() {
		return mission;
	}
}
