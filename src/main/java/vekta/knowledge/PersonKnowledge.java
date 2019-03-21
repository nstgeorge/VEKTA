package vekta.knowledge;

import vekta.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.object.SpaceObject;
import vekta.person.Person;

public class PersonKnowledge extends SpaceObjectKnowledge {
	private final Person person;

	public PersonKnowledge(ObservationLevel level, Person person) {
		super(level);

		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public int getArchiveValue() {
		return 0;
	}

	@Override
	public String getName() {
		return getPerson().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().findHomeObject();
	}

	@Override
	public boolean isSimilar(ObservationKnowledge other) {
		return other instanceof PersonKnowledge && getPerson() == ((PersonKnowledge)other).getPerson();
	}

	@Override
	public boolean isValid(Player player) {
		return !getPerson().isDead();
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		layout.add(new TextDisplay("Home Planet: " + getSpaceObject().getName()))
				.customize().color(getSpaceObject().getColor());
	}
}
