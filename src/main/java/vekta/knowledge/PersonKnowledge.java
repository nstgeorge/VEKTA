package vekta.knowledge;

import vekta.Player;
import vekta.object.SpaceObject;
import vekta.person.Person;

import static vekta.Vekta.v;

public class PersonKnowledge extends SpaceObjectKnowledge {
	private final Person person;

	public PersonKnowledge(KnowledgeLevel level, Person person) {
		super(level);

		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().findHomeObject();
	}

	@Override
	public boolean isValid(Player player) {
		return !getPerson().isDead();
	}

	@Override
	public void draw(Player player, float width, float height) {
		v.color(getSpaceObject().getColor());
		v.text("Home Planet: " + getSpaceObject().getName(), 0, 0);
	}
}
