package vekta.situation;

import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.BlackHole;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.sound.SoundGroup;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.*;

public class BlackHoleSituation implements Situation {

	private static final float MAX_DISTANCE = 30 * AU_DISTANCE;

	@Override
	public boolean isHappening(Player player) {
		SpaceObject orbitObject = getWorld().findOrbitObject(player.getShip());
		return orbitObject instanceof BlackHole && orbitObject.relativePosition(player.getShip()).magSq() <= sq(MAX_DISTANCE);
	}

	@Override
	public void start(Player player) {
		Resources.setMusic("singularity", false);
	}

	@Override
	public void during(Player player) {
	}

	@Override
	public void end(Player player) {
		Resources.stopMusic();
	}
}
