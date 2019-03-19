package vekta.object.ship;

import processing.core.PVector;
import vekta.Faction;
import vekta.object.SpaceObject;

public class PatrolShip extends FighterShip {
	private final Faction faction;

	public PatrolShip(Faction faction, PVector heading, PVector position, PVector velocity) {
		super(faction.getName() + " Patrol", heading, position, velocity, faction.getColor());

		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		if(obj instanceof ModularShip && !obj.isDestroyed()) {
			ModularShip ship = (ModularShip)obj;
			if(ship.hasController() && getFaction().isEnemy(ship.getController().getFaction())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public float getAttackScale() {
		return 2;
	}
}
