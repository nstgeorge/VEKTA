package vekta.dungeon;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.option.DialogButton;
import vekta.person.Person;
import vekta.person.TemporaryPerson;
import vekta.spawner.StoryGenerator;
import vekta.story.part.Story;

public class StoryRoom extends DungeonRoom {
	private final Story story;

	public StoryRoom(DungeonRoom parent, Story story) {
		super(parent, Resources.generateString("dungeon_story_room"), "You notice words inscribed along the walls of the room.");

		this.story = story;
	}

	public Story getStory() {
		return story;
	}

	@Override
	public void onMenu(Menu menu) {
		Person person = new TemporaryPerson(getName(), menu.getPlayer().getFaction());
		menu.add(new DialogButton("Read Story", StoryGenerator.createDialog(person, getStory())));
	}
}
