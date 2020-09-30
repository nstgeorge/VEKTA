package vekta.knowledge;

import vekta.player.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.display.VerticalLayout;
import vekta.object.SpaceObject;
import vekta.person.Person;
import vekta.terrain.settlement.Settlement;

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
		Layout home = layout.add(new VerticalLayout());
		home.customize().spacing(layout.getStyle().spacing() / 2);

		Settlement settlement = getPerson().findHome();
		home.add(new TextDisplay("Home: " + settlement.getName()))
				.customize().color(settlement.getColor());

		home.add(new TextDisplay("Planet: " + getSpaceObject().getName()))
				.customize().color(getSpaceObject().getColor());

		layout.add(new TextDisplay("Enjoys: " + String.join(", ", getPerson().getInterests())))
				.customize().color(100);
	}
}
