package vekta.dungeon;

import vekta.knowledge.PersonKnowledge;
import vekta.menu.Menu;
import vekta.menu.option.DialogButton;
import vekta.person.Person;
import vekta.util.Debounce;

public class PersonRoom extends DungeonRoom {

	private final Person person;

	private final Debounce visitDebounce = new Debounce(60 * 10);

	public PersonRoom(DungeonRoom parent, Person person) {
		super(parent, person.getName(), "You see a dark silhouette in the corner.");

		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public boolean isEnabled() {
		return visitDebounce.isReady();
	}

	@Override
	public void onMenu(Menu menu) {
		visitDebounce.reset();

		String type = "unknown";
		if(menu.getPlayer().hasKnowledge(PersonKnowledge.class, k -> k.getPerson() == getPerson())) {
			type = "greeting";
		}
		if(!person.getOpinion(menu.getPlayer().getFaction()).isPositive()) {
			type = "acting_suspicious";
		}
		
		menu.add(new DialogButton("Talk to Person", getPerson().createDialog(type)));
	}
}
