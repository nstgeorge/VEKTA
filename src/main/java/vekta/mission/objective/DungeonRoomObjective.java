package vekta.mission.objective;

import vekta.dungeon.DungeonRoom;
import vekta.menu.Menu;
import vekta.menu.handle.DungeonMenuHandle;
import vekta.object.SpaceObject;

public class DungeonRoomObjective extends Objective {
	private final DungeonRoom room;

	public DungeonRoomObjective(DungeonRoom room) {
		this.room = room;
	}

	public DungeonRoom getRoom() {
		return room;
	}

	@Override
	public String getName() {
		return "Explore the " + getRoom().getName() + " in " + getRoom().getDungeon().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getRoom().getDungeon().getSite().getParent();
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DungeonMenuHandle) {
			DungeonRoom room = ((DungeonMenuHandle)menu.getHandle()).getRoom();
			// Soft room name equality check
			if(room.getDungeon() == getRoom().getDungeon() && room.getName().equals(getRoom().getName())) {
				complete();
			}
		}
	}
}
