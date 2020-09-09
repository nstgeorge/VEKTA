package vekta.situation;

import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.BlackHole;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.sound.SoundGroup;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.DANGER_COLOR;
import static vekta.Vekta.getWorld;

public class BlackHoleSituation implements Situation {
	//	private static final SoundGroup SINGULARITY_MUSIC = new SoundGroup("singularity");

	@Override
	public boolean isHappening(Player player) {
		return getWorld().findOrbitObject(player.getShip()) instanceof BlackHole;
	}

	@Override
	public void start(Player player) {
		//		Resources.setMusic(SINGULARITY_MUSIC.random(), true);
	}

	@Override
	public void during(Player player) {
	}

	@Override
	public void end(Player player) {
		//		Resources.stopMusic();
	}
}
