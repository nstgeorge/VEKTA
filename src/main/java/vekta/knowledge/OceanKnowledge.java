package vekta.knowledge;

import vekta.display.Layout;
import vekta.display.OceanDisplay;
import vekta.display.TextDisplay;
import vekta.item.Inventory;
import vekta.object.SpaceObject;
import vekta.player.Player;
import vekta.terrain.location.OceanLocation;

public class OceanKnowledge extends SpaceObjectKnowledge {
	private static final float PREVIEW_HEIGHT = 100;

	private final OceanLocation location;
	private final Inventory inventory = new Inventory();

	public OceanKnowledge(ObservationLevel level, OceanLocation location) {
		super(level);

		this.location = location;
	}

	public OceanLocation getLocation() {
		return location;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getName() {
		return "Ocean (" + super.getName() + ")";
	}

	@Override
	public int getArchiveValue() {
		return 5;
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getLocation().getPlanet();
	}

	@Override
	public boolean isSimilar(ObservationKnowledge other) {
		return other instanceof OceanKnowledge && getSpaceObject() == ((SpaceObjectKnowledge)other).getSpaceObject();
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		layout.customize().spacing(layout.getStyle().spacing() * 2);

		layout.add(new OceanDisplay(getColor(player), PREVIEW_HEIGHT));

		layout.add(new TextDisplay("Planet: " + getSpaceObject().getName()));
	}
}
