package vekta.situation;

import vekta.faction.PlayerFaction;
import vekta.person.Person;
import vekta.player.Player;
import vekta.spawner.MissionGenerator;
import vekta.spawner.event.ApproachEventSpawner;

import static vekta.Vekta.getWorld;

public class RechargeSituation implements Situation {

	public static Person findRechargePerson() {
		return getWorld().findObjects(Person.class).stream()
				.filter(p -> !p.isDead() && !(p.getFaction() instanceof PlayerFaction))
				.findFirst().orElseGet(MissionGenerator::randomMissionPerson);
	}

	@Override
	public boolean isHappening(Player player) {
		return player.getShip().getEnergy() <= 0;
	}

	@Override
	public void start(Player player) {
		ApproachEventSpawner.createMessenger(player, findRechargePerson().createDialog("recharge"));
	}
}
