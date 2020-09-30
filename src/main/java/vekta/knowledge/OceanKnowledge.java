package vekta.knowledge;

import vekta.player.Player;
import vekta.display.Layout;
import vekta.display.OceanDisplay;
import vekta.display.TextDisplay;
import vekta.item.Inventory;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;

public class OceanKnowledge extends SpaceObjectKnowledge {
	private static final float PREVIEW_HEIGHT = 100;

	private final LandingSite site;
	private final Inventory inventory = new Inventory();

	public OceanKnowledge(ObservationLevel level, LandingSite site) {
		super(level);

		this.site = site;
	}

	public LandingSite getSite() {
		return site;
	}

	public Terrain getTerrain() {
		return getSite().getTerrain();
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
		return getSite().getParent();
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
