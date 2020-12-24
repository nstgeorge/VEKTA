package vekta.terrain.settlement.building;

import vekta.menu.Menu;
import vekta.menu.option.DialogButton;
import vekta.menu.option.RenameButton;
import vekta.menu.option.UpgradeMenuButton;
import vekta.person.Person;
import vekta.person.TemporaryPerson;
import vekta.spawner.objective.AdviceObjectiveSpawner;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.SettlementPart;

public class CapitalBuilding implements SettlementPart {
	private final String title;
	private final Settlement settlement;

	public CapitalBuilding(String title, Settlement settlement) {
		this.title = title;
		this.settlement = settlement;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return title;
	}

	@Override
	public BuildingType getType() {
		return BuildingType.GOVERNMENT;
	}

	@Override
	public void setupMenu(Menu menu) {
		if(getSettlement().getFaction() == menu.getPlayer().getFaction()) {
			// Add upgrade management option
			menu.add(new UpgradeMenuButton(menu.getPlayer(), getSettlement()));

			// Add advisement option
			Person person = new TemporaryPerson(title + " of " + getSettlement().getName(), getSettlement().getFaction());
			menu.add(new DialogButton("Advise " + title, AdviceObjectiveSpawner.randomAdviceDialog(menu.getPlayer(), person)));

			menu.add(new RenameButton(getSettlement()));
		}
	}
}
