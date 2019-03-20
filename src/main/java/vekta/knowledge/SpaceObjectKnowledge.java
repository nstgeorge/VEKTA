package vekta.knowledge;

import vekta.KeyBinding;
import vekta.Player;
import vekta.Settings;
import vekta.object.SpaceObject;
import vekta.overlay.singleplayer.TelemetryOverlay;

import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static vekta.Vekta.v;

public abstract class SpaceObjectKnowledge extends ObservationKnowledge {
	protected static final int SPACING = 50;
	private static final int PREVIEW_SIZE = 100;

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
	public void draw(Player player, float width, float height) {
		if(ObservationLevel.SCANNED.isAvailableFrom(getLevel())) {
			v.noFill();

			// Draw object preview
			v.pushMatrix();
			v.translate(width / 2, PREVIEW_SIZE / 2F);
			getSpaceObject().drawPreview(PREVIEW_SIZE);
			v.popMatrix();
		}
		else {
			v.textSize(PREVIEW_SIZE);
			v.textAlign(CENTER);

			v.fill(100);
			v.text("?", width / 2, PREVIEW_SIZE / 2F);
			v.textSize(24);

			v.textAlign(LEFT);
		}
		v.fill(getColor(player));
		v.translate(0, PREVIEW_SIZE * 2 + SPACING);
	}
}
