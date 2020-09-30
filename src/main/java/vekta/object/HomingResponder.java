package vekta.object;

import vekta.object.ship.Damageable;
import vekta.object.ship.Damager;

public interface HomingResponder extends Damageable {
	void respondIncoming(Damager damager);
}
