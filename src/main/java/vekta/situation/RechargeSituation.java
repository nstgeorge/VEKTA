package vekta.situation;

import vekta.player.Player;
import vekta.spawner.MissionGenerator;
import vekta.spawner.event.ApproachEventSpawner;

public class RechargeSituation implements Situation {
	@Override
	public boolean isHappening(Player player) {
		return player.getShip().getEnergy() <= 0;
	}

	@Override
	public void start(Player player) {
		ApproachEventSpawner.createMessenger(player, MissionGenerator.randomMissionPerson().createDialog("recharge"));
	}
}
