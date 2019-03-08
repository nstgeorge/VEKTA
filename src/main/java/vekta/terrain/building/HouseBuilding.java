package vekta.terrain.building;

import vekta.MissionGenerator;
import vekta.menu.Menu;
import vekta.menu.option.DialogOption;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.terrain.Terrain;
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
		return getPerson().getDisplayName() + "'s House";
	}

	@Override
	public void setupTerrain(Terrain terrain) {
	}

	@Override
	public void setupLandingMenu(Menu menu) {
		if(getPerson().getOpinion(menu.getPlayer()).isPositive()) {
			Dialog dialog = MissionGenerator.randomVisitDialog(menu.getPlayer(), getPerson());
			menu.add(new DialogOption("Visit " + getPerson().getDisplayName(), dialog));
		}
	}
}
