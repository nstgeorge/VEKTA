package vekta.knowledge;

import vekta.Player;
import vekta.object.ship.Ship;

public class ShipKnowledge extends SpaceObjectKnowledge {
	private final Ship ship;

	public ShipKnowledge(KnowledgeLevel level, Ship ship) {
		super(level);

		this.ship = ship;
	}

	@Override
	public Ship getSpaceObject() {
		return ship;
	}
	
	@Override
	public void draw(Player player, float width, float height) {
		// Draw ship preview
		super.draw(player, width, height);
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
