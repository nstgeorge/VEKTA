package vekta.knowledge;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;
import vekta.display.Layout;
import vekta.display.SpaceObjectDisplay;
import vekta.menu.Menu;
import vekta.menu.handle.KnowledgeMenuHandle;
import vekta.menu.option.BackButton;
import vekta.menu.option.TargetButton;
import vekta.object.SpaceObject;
import vekta.player.Player;

import static vekta.Vekta.*;

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
		return distanceString(player.getShip().relativePosition(getSpaceObject()).mag());
	}

	@Override
	public boolean isValid(Player player) {
		return !getSpaceObject().isDestroyed();
	}

	@Override
	public String getCursorText(Player player) {
		if(player.getShip().findNavigationTarget() == getSpaceObject()) {
			return ":: Targeted ::";
		}
		return Settings.getKeyText(KeyBinding.SHIP_TARGET) + " to set target, " + Settings.getKeyText(KeyBinding.MENU_SELECT) + " for options";
	}

	@Override
	public void onKeyPress(Player player, KeyBinding key) {
		if(key == KeyBinding.MENU_SELECT) {
			Menu menu = new Menu(player, new BackButton(getContext()), new KnowledgeMenuHandle(this));
			onMenu(menu);
			menu.addDefault();
			setContext(menu);
		}
		else if(key == KeyBinding.SHIP_TARGET) {
			player.getShip().setNavigationTarget(getSpaceObject());
			Resources.playSound("select");
		}
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		SpaceObject displayObject = ObservationLevel.SCANNED.isAvailableFrom(getLevel()) ? getSpaceObject() : null;
		layout.add(new SpaceObjectDisplay(displayObject, PREVIEW_SIZE));
	}

	public void onMenu(Menu menu) {
		menu.add(new TargetButton(getSpaceObject()));
	}
}
