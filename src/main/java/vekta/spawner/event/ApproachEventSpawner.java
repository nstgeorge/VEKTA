package vekta.spawner.event;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.object.ship.MessengerShip;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.spawner.EventGenerator;
import vekta.spawner.MissionGenerator;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.register;

public class ApproachEventSpawner implements EventGenerator.EventSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public void spawn(Player player) {
		createMessenger(player, DialogGenerator.randomApproachDialog(player, MissionGenerator.randomMissionPerson()));
	}

	public static MessengerShip createMessenger(Player player, Dialog dialog) {
		PVector pos = WorldGenerator.randomSpawnPosition(RenderLevel.SHIP, player.getShip().getPosition());
		MessengerShip ship = register(new MessengerShip(
				player,
				dialog,
				dialog != null ? dialog.getPerson().getFullName() : "HERMES II",
				PVector.random2D(),
				pos,
				player.getShip().getVelocity(),
				dialog != null ? dialog.getPerson().getColor() : WorldGenerator.randomPlanetColor()));
		player.send(ship.getName() + " approaches with a message!")
				.withColor(ship.getColor())
				.withTime(2);
		return ship;
	}
}
