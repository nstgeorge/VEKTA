package vekta.knowledge;

import vekta.KeyBinding;
import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.option.ShipSwitchButton;
import vekta.object.ship.ModularShip;
import vekta.object.ship.Ship;

public class ShipKnowledge extends SpaceObjectKnowledge {
	private final Ship ship;

	public ShipKnowledge(ObservationLevel level, Ship ship) {
		super(level);

		this.ship = ship;
	}

	@Override
	public int getArchiveValue() {
		return 0;
	}

	@Override
	public Ship getSpaceObject() {
		return ship;
	}

	@Override
	public boolean isSimilar(ObservationKnowledge other) {
		return other instanceof ShipKnowledge && getSpaceObject() == ((ShipKnowledge)other).getSpaceObject();
	}

	@Override
	public String getSecondaryText(Player player) {
		if(player.getShip() == getSpaceObject()) {
			return null;
		}
		return super.getSecondaryText(player);
	}

	@Override
	public String getCursorText(Player player) {
		if(player.getShip() == getSpaceObject()) {
			return null;
		}
		return super.getCursorText(player);
	}

	@Override
	public void onKeyPress(Player player, KeyBinding key) {
		if(player.getShip() == getSpaceObject()) {
			return;
		}
		super.onKeyPress(player, key);
	}

	@Override
	public void onMenu(Menu menu) {
		super.onMenu(menu);

		if(ship instanceof ModularShip && getLevel() == ObservationLevel.OWNED) {
			menu.add(new ShipSwitchButton((ModularShip)ship));
		}
	}
}
