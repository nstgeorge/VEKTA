package vekta;

import processing.core.PVector;
import vekta.menu.dialog.Dialog;
import vekta.object.ship.MessengerShip;
import vekta.overlay.singleplayer.Notification;

import static vekta.Vekta.addObject;

public class MissionGenerator {
	public static MessengerShip createMessenger(Player player, Dialog dialog) {
		PVector pos = WorldGenerator.randomSpawnPosition(RenderLevel.AROUND_SHIP, player.getShip().getPosition());
		MessengerShip ship = new MessengerShip(
				player,
				dialog,
				"HERMES II",
				PVector.random2D(),
				pos,
				player.getShip().getVelocity(),
				WorldGenerator.randomPlanetColor());//TODO: use character/faction color
		addObject(ship);
		player.send(new Notification(ship.getName() + " approaches with a message!")
				.withColor(ship.getColor()));
		return ship;
	}
}  
