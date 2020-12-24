package vekta.terrain.settlement.building;

import vekta.Resources;
import vekta.deity.Deity;
import vekta.menu.Menu;
import vekta.menu.option.TempleButton;
import vekta.terrain.settlement.SettlementPart;

public class TempleBuilding implements SettlementPart {
	private final Deity deity;
	private String name;
	private boolean offered;

	public TempleBuilding(Deity deity) {
		this.deity = deity;
		this.name = Resources.generateString("temple_decorator").replace("*", deity.getName());
	}

	public Deity getDeity() {
		return deity;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getGenericName() {
		return "Temple";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.KNOWLEDGE;
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.add(new TempleButton(getName(), getDeity()));
	}
}
