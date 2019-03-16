package vekta.terrain.building;

import vekta.menu.Menu;
import vekta.menu.option.DialogOption;
import vekta.menu.option.UpgradeMenuOption;
import vekta.person.Person;
import vekta.spawner.objective.AdviceObjectiveSpawner;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.SettlementPart;

public class CapitalBuilding implements SettlementPart {
	private final Settlement settlement;

	public CapitalBuilding(Settlement settlement) {
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
		return "Town Hall";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.GOVERNMENT;
	}

	@Override
	public void setup(LandingSite site) {
	}

	@Override
	public void setupMenu(Menu menu) {
		if(getSettlement().getFaction() == menu.getPlayer().getFaction()) {
			// Add upgrade management option
			menu.add(new UpgradeMenuOption(menu.getPlayer(), getSettlement()));

			// Add advisement option
			Person person = new Person("Mayor of " + getSettlement().getName(), getSettlement().getFaction());
			menu.add(new DialogOption("Advise Mayor", AdviceObjectiveSpawner.randomAdviceDialog(menu.getPlayer(), person)));
		}
	}
}
