package vekta.menu.option;

import vekta.dungeon.DungeonRoom;
import vekta.menu.Menu;
import vekta.menu.handle.DungeonMenuHandle;

import static vekta.Vekta.*;

public class DungeonRoomOption implements MenuOption {
	private final String name;
	private final DungeonRoom room;

	public DungeonRoomOption(String name, DungeonRoom room) {
		this.name = name;
		this.room = room;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor() {
		return room.isVisited() ? UI_COLOR : v.color(200, 200, 100);
	}

	@Override
	public void onSelect(Menu menu) {
		room.setVisited(true);
		Menu sub = new Menu(menu, new DungeonMenuHandle(room));
		for(String path : room.getPathMap().keySet()) {
			sub.add(new DungeonRoomOption(path, room.getPathMap().get(path)));
		}
		setContext(sub);
		applyContext();
		sub.addDefault();
	}
}
