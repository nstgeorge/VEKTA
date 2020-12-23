package vekta.situation;

import vekta.player.Player;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.sound.SoundGroup;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.DANGER_COLOR;
import static vekta.Vekta.getWorld;

public class HostileSituation implements Situation {
	private static final SoundGroup HOSTILE_MUSIC = new SoundGroup("hostile");

	@Override
	public boolean isHappening(Player player) {
		SpaceObject orbit = getWorld().findOrbitObject(player.getShip());
		if(orbit instanceof TerrestrialPlanet) {
			LandingSite site = ((TerrestrialPlanet)orbit).getLandingSite();
			for(Settlement settlement : site.getTerrain().findVisitableSettlements()) {
				if(settlement.getFaction().isEnemy(player.getFaction())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void start(Player player) {
		Resources.setMusic(HOSTILE_MUSIC.random(), true);
		player.send("Entering hostile territory!")
				.withColor(DANGER_COLOR)
				.withTime(2);
	}

	@Override
	public void during(Player player) {
	}

	@Override
	public void end(Player player) {
		Resources.stopMusic();
		player.send("Leaving hostile territory");
	}
}
