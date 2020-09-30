package vekta.mission.objective;

import vekta.item.ItemType;
import vekta.menu.Menu;
import vekta.menu.handle.SettlementMenuHandle;
import vekta.menu.option.CustomButton;
import vekta.object.SpaceObject;
import vekta.terrain.settlement.Settlement;

public class SettlementTaskObjective extends Objective {
	private final String name;
	private final Settlement settlement;

	public SettlementTaskObjective(String name) {
		this(name, null);
	}

	public SettlementTaskObjective(String name, Settlement settlement) {
		this.name = name;
		this.settlement = settlement;
	}

	@Override
	public String getName() {
		return name + (getSettlement() != null ? " (" + getSettlement().getName() + ")" : "");
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getSettlement() != null ? getSettlement().getParent() : null;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof SettlementMenuHandle) {
			if(getSettlement() == null || getSettlement() == ((SettlementMenuHandle)menu.getHandle()).getSettlement()) {
				menu.add(new CustomButton(name, m -> complete())
						.withColor(ItemType.KNOWLEDGE.getColor())
						.withRemoval());
			}
		}
	}
}
