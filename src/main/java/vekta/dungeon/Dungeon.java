package vekta.dungeon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dungeon implements Serializable {
	private final String name;
	private DungeonRoom startRoom;
	
	private List<DungeonRoom> rooms = new ArrayList<>();

	public Dungeon(String name, String description) {
		this.name = name;
		this.startRoom = new DungeonStartRoom(this, description);
	}

	public String getName() {
		return name;
	}

	public DungeonRoom getStartRoom() {
		return startRoom;
	}

	public List<DungeonRoom> getRooms() {
		return rooms;
	}
}
