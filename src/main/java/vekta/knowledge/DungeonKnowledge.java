package vekta.knowledge;

import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.dungeon.Dungeon;
import vekta.item.ItemType;
import vekta.object.SpaceObject;
import vekta.player.Player;

import static vekta.Vekta.v;

public class DungeonKnowledge extends SpaceObjectKnowledge {

	private final Dungeon dungeon;

	public DungeonKnowledge(ObservationLevel level, Dungeon dungeon) {
		super(level);

		this.dungeon = dungeon;
	}

	@Override
	public int getArchiveValue() {
		if(ObservationLevel.VISITED.isAvailableFrom(getLevel())) {
			return 5 + getDungeon().getRooms().size();
		}
		return 2;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getDungeon().getLocation().getPlanet();
	}

	@Override
	public boolean isSimilar(ObservationKnowledge other) {
		return other instanceof DungeonKnowledge && getDungeon() == ((DungeonKnowledge)other).getDungeon();
	}

	@Override
	public String getName() {
		if(getLevel() == ObservationLevel.AWARE) {
			return "(Unexplored)";
		}
		return getDungeon().getName();
	}

	@Override
	public int getColor(Player player) {
		if(getLevel() == ObservationLevel.AWARE) {
			return v.color(100);
		}
		return ItemType.RARE.getColor();
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		layout.add(new TextDisplay("Planet: " + getSpaceObject().getName()))
				.customize().color(getSpaceObject().getColor());

		//		if(ObservationLevel.SCANNED.isAvailableFrom(getLevel())) {
		//			Layout scanned = layout.add(new VerticalLayout());
		//			scanned.customize().spacing(layout.getStyle().spacing() / 2);
		//
		//		}
	}
}
