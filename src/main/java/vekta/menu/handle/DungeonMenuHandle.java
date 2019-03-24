package vekta.menu.handle;

import vekta.dungeon.DungeonRoom;
import vekta.menu.Menu;

import static vekta.Vekta.v;

public class DungeonMenuHandle extends MenuHandle {
	private final DungeonRoom room;

	public DungeonMenuHandle(DungeonRoom room) {
		this.room = room;
	}

	public DungeonRoom getRoom() {
		return room;
	}

	@Override
	public void focus(Menu menu) {
		super.focus(menu);

		getRoom().onEnter(menu);
	}

	@Override
	public void unfocus(Menu menu) {
		super.unfocus(menu);

		getRoom().onLeave(menu);
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		getRoom().onUpdate(menu);

		v.textSize(48);
		v.fill(100);
		v.text(room.getName(), getButtonX(), getButtonY(-3));

		v.textSize(32);
		v.fill(220);
		v.text(room.getDescription(), getButtonX(), getButtonY(-2));
	}
}
