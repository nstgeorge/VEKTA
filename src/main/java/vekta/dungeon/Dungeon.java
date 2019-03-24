package vekta.dungeon;

import vekta.terrain.LandingSite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dungeon implements Serializable {
	private final LandingSite site;
	private final String name;
	private DungeonRoom startRoom;

	private List<DungeonRoom> rooms = new ArrayList<>();

	public Dungeon(LandingSite site, String name, String description) {
		this.site = site;
		this.name = name;
		this.startRoom = new DungeonStartRoom(this, description);
	}

	public LandingSite getSite() {
		return site;
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
