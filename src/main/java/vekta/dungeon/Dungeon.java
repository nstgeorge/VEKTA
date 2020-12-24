package vekta.dungeon;

import vekta.terrain.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dungeon implements Serializable {
	private final Location location;
	private final String name;
	private final DungeonRoom startRoom;

	private final List<DungeonRoom> rooms = new ArrayList<>();

	public Dungeon(Location location, String name, String description) {
		this.location = location;
		this.name = name;
		this.startRoom = new DungeonStartRoom(this, description);
	}

	public Location getLocation() {
		return location;
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
