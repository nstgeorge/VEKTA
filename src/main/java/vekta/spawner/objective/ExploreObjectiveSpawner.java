package vekta.spawner.objective;

import vekta.dungeon.DungeonRoom;
import vekta.mission.Mission;
import vekta.mission.objective.DungeonRoomObjective;
import vekta.mission.objective.Objective;
import vekta.spawner.DungeonGenerator;

import static vekta.spawner.MissionGenerator.ObjectiveSpawner;

public class ExploreObjectiveSpawner implements ObjectiveSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 2;
	}

	@Override
	public Objective getMainObjective(Mission mission) {
		DungeonRoom room = DungeonGenerator.randomRoom();
		return new DungeonRoomObjective(room);
	}
}
