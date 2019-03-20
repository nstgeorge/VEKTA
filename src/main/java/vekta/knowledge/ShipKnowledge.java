package vekta.knowledge;

import vekta.Player;
import vekta.object.ship.Ship;

public class ShipKnowledge extends SpaceObjectKnowledge {
	private final Ship ship;

	public ShipKnowledge(ObservationLevel level, Ship ship) {
		super(level);

		this.ship = ship;
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
			return "";
		}
		return super.getSecondaryText(player);
	}

	@Override
	public String getSelectText(Player player) {
		if(player.getShip() == getSpaceObject()) {
			return "";
		}
		return super.getSelectText(player);
	}

	@Override
	public void onSelect(Player player) {
		if(player.getShip() == getSpaceObject()) {
			return;
		}
		super.onSelect(player);
	}
}
