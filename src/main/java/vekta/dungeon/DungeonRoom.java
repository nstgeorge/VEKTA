package vekta.dungeon;

import vekta.menu.Menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DungeonRoom implements Serializable {
	private final Dungeon dungeon;
	private final String name;
	private final String description;
	private final Map<String, DungeonRoom> connections = new HashMap<>();

	public DungeonRoom(DungeonRoom parent, String name, String description) {
		this(parent.getDungeon(), name, description);
	}

	public DungeonRoom(Dungeon dungeon, String name, String description) {
		this.dungeon = dungeon;
		this.name = name;
		this.description = description;

		dungeon.getRooms().add(this); // Register room
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, DungeonRoom> getPathMap() {
		return connections;
	}

	public void addPath(String name, DungeonRoom room) {
		connections.put(name, room);
	}

	public void onEnter(Menu menu) {
	}

	public void onLeave(Menu menu) {
	}

	public void onUpdate(Menu menu) {
	}
}
