package vekta.situation;

import vekta.Player;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.DANGER_COLOR;
import static vekta.Vekta.getWorld;

public class HostileSituation implements Situation {
	@Override
	public boolean isHappening(Player player) {
		SpaceObject orbit = getWorld().findOrbitObject(player.getShip());
		if(orbit instanceof TerrestrialPlanet) {
			LandingSite site = ((TerrestrialPlanet)orbit).getLandingSite();
			for(Settlement settlement : site.getTerrain().getSettlements()) {
				if(settlement.getFaction().isEnemy(player.getFaction())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void start(Player player) {
		player.send("Entering hostile territory!")
				.withColor(DANGER_COLOR)
				.withTime(2);
	}
	
	@Override
	public void during(Player player) {
		
	}

	@Override
	public void end(Player player) {
		player.send("Leaving hostile territory");
	}
}
