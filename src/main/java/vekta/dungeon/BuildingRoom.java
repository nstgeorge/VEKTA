package vekta.dungeon;

import vekta.menu.Menu;
import vekta.terrain.settlement.SettlementPart;

public class BuildingRoom extends DungeonRoom {
	private final SettlementPart building;

	public BuildingRoom(DungeonRoom parent, SettlementPart building, String description) {
		super(parent, building.getName(), description);

		this.building = building;
	}

	@Override
	public void onMenu(Menu menu) {
		building.setupMenu(menu);
	}
}
