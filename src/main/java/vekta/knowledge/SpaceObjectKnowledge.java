package vekta.knowledge;

import vekta.KeyBinding;
import vekta.Player;
import vekta.Settings;
import vekta.display.Layout;
import vekta.display.SpaceObjectDisplay;
import vekta.object.SpaceObject;
import vekta.overlay.singleplayer.TelemetryOverlay;

public abstract class SpaceObjectKnowledge extends ObservationKnowledge {
	private static final int PREVIEW_SIZE = 200;

	public SpaceObjectKnowledge(ObservationLevel level) {
		super(level);
	}

	public abstract SpaceObject getSpaceObject();

	@Override
	public String getName() {
		return getSpaceObject().getName();
	}

	@Override
	public int getColor(Player player) {
		return getSpaceObject().getColor();
	}

	@Override
	public String getSecondaryText(Player player) {
		return TelemetryOverlay.getDistanceString(player.getShip().relativePosition(getSpaceObject()).mag());
	}

	@Override
	public boolean isValid(Player player) {
		return !getSpaceObject().isDestroyed();
	}

	@Override
	public String getSelectText(Player player) {
		if(player.getShip().findNavigationTarget() == getSpaceObject()) {
			return ":: Targeted ::";
		}
		return Settings.getKeyText(KeyBinding.MENU_SELECT) + " to set target";
	}

	@Override
	public void onSelect(Player player) {
		player.getShip().setNavigationTarget(getSpaceObject());
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		SpaceObject displayObject = ObservationLevel.SCANNED.isAvailableFrom(getLevel()) ? getSpaceObject() : null;
		layout.add(new SpaceObjectDisplay(displayObject, PREVIEW_SIZE));
	}
}
