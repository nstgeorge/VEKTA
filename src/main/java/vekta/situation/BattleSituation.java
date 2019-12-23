package vekta.situation;

import processing.sound.SoundFile;
import vekta.Player;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.object.ship.FighterShip;
import vekta.sound.SoundGroup;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.getDistanceUnit;
import static vekta.Vekta.getWorld;

public class BattleSituation implements Situation {
	private static final SoundGroup BATTLE_MUSIC = new SoundGroup("battle");
	private static final float BATTLE_RADIUS = WorldGenerator.getRadius(RenderLevel.SHIP);

	@Override
	public boolean isHappening(Player player) {
		for(FighterShip ship : getWorld().findObjects(FighterShip.class)) {
			if(ship.getColor() != player.getColor() && ship.getTarget() != null && ship.getTarget().getColor() == player.getColor()) {
				if(player.getShip().relativePosition(ship).magSq() <= BATTLE_RADIUS * BATTLE_RADIUS) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void start(Player player) {
		SoundFile sound = BATTLE_MUSIC.random();
		Resources.setMusic(sound, true);

		during(player);
	}

	@Override
	public void during(Player player) {
		getWorld().setMaxZoom(getDistanceUnit(RenderLevel.SHIP));
	}

	@Override
	public void end(Player player) {
		Resources.stopMusic();

		getWorld().setMaxZoom(Float.POSITIVE_INFINITY);
	}
}
