package vekta.menu.handle;

import vekta.dungeon.DungeonRoom;

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
	public void focus() {
		super.focus();

		getRoom().onEnter(getMenu());
	}

	@Override
	public void unfocus() {
		super.unfocus();

		getRoom().onLeave(getMenu());
	}

	@Override
	public void render() {
		super.render();

		getRoom().onUpdate(getMenu());

		v.textSize(48);
		v.fill(100);
		v.text(room.getName(), getItemX(), getItemY(-2));

		v.textSize(32);
		v.fill(220);
		v.text(room.getDescription(), getItemX(), getItemY(-1));
	}
}
