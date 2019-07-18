package vekta.dungeon;

import vekta.menu.Menu;
import vekta.menu.option.DialogButton;
import vekta.person.Person;

public class PersonRoom extends DungeonRoom {
	private final Person person;

	public PersonRoom(DungeonRoom parent, Person person) {
		super(parent, person.getName(), "You see a dark silhouette in the corner.");
		
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public void onEnter(Menu menu) {
		if(!getPerson().isDead()) {
			menu.add(new DialogButton("Talk to Person", getPerson().createDialog("dungeon")));
		}
	}

	@Override
	public void onLeave(Menu menu) {
	}
}
