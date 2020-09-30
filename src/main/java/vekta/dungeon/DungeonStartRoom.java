package vekta.dungeon;

import vekta.Resources;
import vekta.context.Context;
import vekta.knowledge.DungeonKnowledge;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.menu.handle.DungeonMenuHandle;
import vekta.sound.SoundGroup;

import static vekta.Vekta.getNextContext;
import static vekta.Vekta.v;

public class DungeonStartRoom extends DungeonRoom {
	private static final SoundGroup MUSIC = new SoundGroup("dungeon");

	private final int musicIndex = (int)v.random(MUSIC.size());

	public DungeonStartRoom(Dungeon dungeon, String description) {
		super(dungeon, dungeon.getName(), description);
	}

	@Override
	public void onMenu(Menu menu) {
		menu.getPlayer().addKnowledge(new DungeonKnowledge(ObservationLevel.VISITED, getDungeon()));
	}

	@Override
	public void onEnter(Menu menu) {
		Resources.setMusic(MUSIC.get(musicIndex), true);
	}

	@Override
	public void onLeave(Menu menu) {
		Context next = getNextContext();
		if(!(next instanceof Menu) || !(((Menu)next).getHandle() instanceof DungeonMenuHandle)) {
			// End music on leaving dungeon
			Resources.stopMusic();
		}
	}
}
