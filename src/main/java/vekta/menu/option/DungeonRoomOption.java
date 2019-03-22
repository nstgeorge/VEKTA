package vekta.menu.option;

import vekta.dungeon.DungeonRoom;
import vekta.menu.Menu;
import vekta.menu.handle.DungeonMenuHandle;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.setContext;

public class DungeonRoomOption implements MenuOption {
	private final String name;
	private final DungeonRoom room;

	private boolean visited;

	public DungeonRoomOption(String name, DungeonRoom room) {
		this.name = name;
		this.room = room;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getBorderColor() {
		return visited ? 200 : UI_COLOR;
	}

	@Override
	public void onSelect(Menu menu) {
		visited = true;
		Menu sub = new Menu(menu, new DungeonMenuHandle(room));
		for(String path : room.getPathMap().keySet()) {
			sub.add(new DungeonRoomOption(path, room.getPathMap().get(path)));
		}
		sub.addDefault();
		setContext(sub);
	}
}
