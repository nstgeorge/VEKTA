package vekta.situation;

import vekta.player.Player;
import vekta.menu.option.RechargeButton;
import vekta.person.Dialog;
import vekta.spawner.MissionGenerator;
import vekta.spawner.event.ApproachEventSpawner;

public class RechargeSituation implements Situation {
	@Override
	public boolean isHappening(Player player) {
		return player.getShip().getEnergy() <= 0;
	}

	@Override
	public void start(Player player) {
		Dialog dialog = MissionGenerator.randomMissionPerson().createDialog("recharge");
		dialog.add(new RechargeButton(player, .5F));
		ApproachEventSpawner.createMessenger(player, dialog);
	}
}
