package vekta.menu.option;

import processing.core.PVector;
import vekta.Player;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.object.planet.BlackHole;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.*;

public class FastTravelButton implements ButtonOption {
	private final Player player;
	private final BlackHole target;

	public FastTravelButton(Player player, BlackHole target) {
		this.player = player;
		this.target = target;
	}

	@Override
	public String getName() {
		return target.getName() + " (" + distanceString(target.relativePosition(player.getShip()).mag()) + ")";
	}

	@Override
	public int getColor() {
		return target.getColor();
	}

	@Override
	public void onSelect(Menu menu) {
		Resources.playSound("hyperdriveHit");
		player.getShip().setNavigationTarget(target);
		player.getShip().getPositionReference().set(PVector.random2D().mult(target.getRadius() * 10).add(target.getPositionReference()));
		WorldGenerator.orbit(target, player.getShip(), 0);
		// TODO: consolidate menu closing logic
		while(getContext() instanceof Menu) {
			((Menu)getContext()).close();
			applyContext();
		}
		//		target.getLandingSite().land(player.getShip());
	}
}
