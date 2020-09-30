package vekta.dungeon;

import vekta.menu.Menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DungeonRoom implements Serializable {
	private final Dungeon dungeon;

	private String name;
	private String description;

	private final Map<String, DungeonRoom> connections = new HashMap<>();

	private boolean visited;

	public DungeonRoom(DungeonRoom parent, String name, String description) {
		this(parent.getDungeon(), name, description);
	}

	public DungeonRoom(Dungeon dungeon, String name, String description) {
		this.dungeon = dungeon;
		this.name = name;
		this.description = description;

		// Register room
		dungeon.getRooms().add(this);
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Map<String, DungeonRoom> getPathMap() {
		return connections;
	}

	public void addPath(String name, DungeonRoom room) {
		connections.put(name, room);
	}

	public boolean isEnabled() {
		return true;
	}

	public void onMenu(Menu menu) {
	}

	public void onEnter(Menu menu) {
	}

	public void onLeave(Menu menu) {
	}

	public void onUpdate(Menu menu) {
	}
}
