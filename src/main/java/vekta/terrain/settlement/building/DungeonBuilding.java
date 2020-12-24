package vekta.terrain.settlement.building;

import vekta.dungeon.Dungeon;
import vekta.menu.Menu;
import vekta.menu.option.DungeonRoomButton;
import vekta.terrain.settlement.SettlementPart;

public class DungeonBuilding implements SettlementPart {
	private final Dungeon dungeon;

	public DungeonBuilding(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	@Override
	public String getName() {
		return getDungeon().getName();
	}

	@Override
	public String getGenericName() {
		return "Dungeon";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.EXTERNAL;
	}

	@Override
	public void setupMenu(Menu menu) {
		getDungeon().getStartRoom().setVisited(true); // Don't show special outline
		menu.add(new DungeonRoomButton(getDungeon().getName(), getDungeon().getStartRoom()));
	}
}
