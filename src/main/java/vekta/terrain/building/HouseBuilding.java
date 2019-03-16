package vekta.terrain.building;

import vekta.menu.Menu;
import vekta.menu.option.DialogOption;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.spawner.MissionGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

public class HouseBuilding implements SettlementPart {
	private final Person person;

	public HouseBuilding(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public String getName() {
		return getPerson().getFullName() + "'s House";
	}

	@Override
	public String getTypeString() {
		return "House";
	}

	@Override
	public void setup(LandingSite site) {
	}

	@Override
	public void setupMenu(Menu menu) {
		if(!getPerson().getOpinion(menu.getPlayer().getFaction()).isNegative()) {
			Dialog dialog = MissionGenerator.randomVisitDialog(menu.getPlayer(), getPerson());
			menu.add(new DialogOption("Visit " + getPerson().getName(), dialog));
		}
	}
}
