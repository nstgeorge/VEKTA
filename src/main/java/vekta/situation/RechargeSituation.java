package vekta.situation;

import vekta.Player;
import vekta.menu.option.RechargeOption;
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
		dialog.add(new RechargeOption(player.getShip(), .3F));
		ApproachEventSpawner.createMessenger(player, dialog);
	}

	@Override
	public void end(Player player) {
	}
}
