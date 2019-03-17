package vekta.object.ship;

import vekta.object.SpaceObject;

public interface Damager {
	SpaceObject getAttackObject();

	SpaceObject getParentObject();
}
